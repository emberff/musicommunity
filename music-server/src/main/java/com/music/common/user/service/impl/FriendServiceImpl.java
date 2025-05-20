package com.music.common.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.music.common.chat.dao.RoomFriendDao;
import com.music.common.chat.domain.entity.RoomFriend;
import com.music.common.chat.service.ChatService;
import com.music.common.chat.service.ContactService;
import com.music.common.chat.service.RoomService;
import com.music.common.chat.service.adapter.MessageAdapter;
import com.music.common.common.domain.vo.req.CursorPageBaseReq;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.CursorPageBaseResp;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.event.UserApplyEvent;
import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RequestHolder;
import com.music.common.user.dao.UserApplyDao;
import com.music.common.user.dao.UserDao;
import com.music.common.user.dao.UserFriendDao;
import com.music.common.user.domain.entity.User;
import com.music.common.user.domain.entity.UserApply;
import com.music.common.user.domain.entity.UserFriend;
import com.music.common.user.domain.vo.request.friend.FriendApplyReq;
import com.music.common.user.domain.vo.request.friend.FriendApproveReq;
import com.music.common.user.domain.vo.request.friend.FriendCheckReq;
import com.music.common.user.domain.vo.response.friend.FriendApplyResp;
import com.music.common.user.domain.vo.response.friend.FriendCheckResp;
import com.music.common.user.domain.vo.response.friend.FriendResp;
import com.music.common.user.domain.vo.response.friend.FriendUnreadResp;
import com.music.common.user.service.FriendService;
import com.music.common.user.service.adapter.FriendAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

import static com.music.common.user.domain.enums.ApplyStatusEnum.WAIT_APPROVAL;


/**
 * @author : limeng
 * @description : 好友
 * @date : 2023/07/19
 */
@Slf4j
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserFriendDao userFriendDao;
    @Autowired
    private UserApplyDao userApplyDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomFriendDao roomFriendDao;

    /**
     * 检查
     * 检查是否是自己好友
     *
     * @param uid     uid
     * @param request 请求
     * @return {@link FriendCheckResp}
     */
    @Override
    public FriendCheckResp check(Long uid, FriendCheckReq request) {
        List<UserFriend> friendList = userFriendDao.getByFriends(uid, request.getUidList());

        Set<Long> friendUidSet = friendList.stream().map(UserFriend::getFriendUid).collect(Collectors.toSet());
        List<FriendCheckResp.FriendCheck> friendCheckList = request.getUidList().stream().map(friendUid -> {
            FriendCheckResp.FriendCheck friendCheck = new FriendCheckResp.FriendCheck();
            friendCheck.setUid(friendUid);
            friendCheck.setIsFriend(friendUidSet.contains(friendUid));
            return friendCheck;
        }).collect(Collectors.toList());
        return new FriendCheckResp(friendCheckList);
    }

    /**
     * 申请好友
     *
     * @param request 请求
     */
    @Override
//    @RedissonLock(key = "#uid")
    public void apply(Long uid, FriendApplyReq request) {
        User user = userDao.getById(request.getTargetUid());
        AssertUtil.isNotEmpty(user, "无此用户!");
        //是否有好友关系
        UserFriend friend = userFriendDao.getByFriend(uid, request.getTargetUid());
        AssertUtil.isEmpty(friend, "你们已经是好友了");
        //是否有待审批的申请记录(自己的)
        UserApply selfApproving = userApplyDao.getFriendApproving(uid, request.getTargetUid());
        if (Objects.nonNull(selfApproving)) {
            log.info("已有好友申请记录,uid:{}, targetId:{}", uid, request.getTargetUid());
            return;
        }
        //是否有待审批的申请记录(别人请求自己的)
        UserApply friendApproving = userApplyDao.getFriendApproving(request.getTargetUid(), uid);
        if (Objects.nonNull(friendApproving)) {
            ((FriendService) AopContext.currentProxy()).applyApprove(uid, new FriendApproveReq(friendApproving.getId()));
            return;
        }
        //申请入库
        UserApply insert = FriendAdapter.buildFriendApply(uid, request);
        userApplyDao.save(insert);
        //申请事件
        applicationEventPublisher.publishEvent(new UserApplyEvent(this, insert));
    }

    /**
     * 分页查询好友申请
     *
     * @param request 请求
     * @return {@link PageBaseResp}<{@link FriendApplyResp}>
     */
    @Override
    public PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq request) {
        IPage<UserApply> userApplyIPage = userApplyDao.friendApplyPage(uid, request.plusPage());
        if (CollectionUtil.isEmpty(userApplyIPage.getRecords())) {
            return PageBaseResp.empty();
        }

        // 1. 获取 uid 列表
        List<Long> uidList = userApplyIPage.getRecords().stream()
                .map(UserApply::getUid)
                .collect(Collectors.toList());
        // 2. 查询用户信息
        Map<Long, User> users = userDao.getBatch(uidList);
//        // 3. 构造 uid -> name 映射
//        Map<Long, String> uidNameMap = users.values().stream()
//                .collect(Collectors.toMap(User::getId, User::getName));

        //将这些申请列表设为已读
        readApples(uid, userApplyIPage);
        //返回消息
        return PageBaseResp.init(userApplyIPage, FriendAdapter.buildFriendApplyList(userApplyIPage.getRecords(), users));
    }

    private void readApples(Long uid, IPage<UserApply> userApplyIPage) {
        List<Long> applyIds = userApplyIPage.getRecords()
                .stream().map(UserApply::getId)
                .collect(Collectors.toList());
        userApplyDao.readApples(uid, applyIds);
    }

    /**
     * 申请未读数
     *
     * @return {@link FriendUnreadResp}
     */
    @Override
    public FriendUnreadResp unread(Long uid) {
        Integer unReadCount = userApplyDao.getUnReadCount(uid);
        return new FriendUnreadResp(unReadCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @RedissonLock(key = "#uid")
    public void applyApprove(Long uid, FriendApproveReq request) {
        UserApply userApply = userApplyDao.getById(request.getApplyId());
        AssertUtil.isNotEmpty(userApply, "不存在申请记录");
        AssertUtil.equal(userApply.getTargetId(), uid, "不存在申请记录");
        AssertUtil.equal(userApply.getStatus(), WAIT_APPROVAL.getCode(), "已同意好友申请");
        //同意申请
        userApplyDao.agree(request.getApplyId());
        //创建双方好友关系
        createFriend(uid, userApply.getUid());
        //创建一个聊天房间
        RoomFriend roomFriend = roomService.createFriendRoom(Arrays.asList(uid, userApply.getUid()));
        //发送一条同意消息。。我们已经是好友了，开始聊天吧
        chatService.sendMsg(MessageAdapter.buildAgreeMsg(roomFriend.getRoomId()), uid);
    }

    /**
     * 删除好友
     *
     * @param uid       uid
     * @param friendUid 朋友uid
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(Long uid, Long friendUid) {
        List<UserFriend> userFriends = userFriendDao.getUserFriend(uid, friendUid);
        if (CollectionUtil.isEmpty(userFriends)) {
            log.info("没有好友关系：{},{}", uid, friendUid);
            return;
        }
        List<Long> friendRecordIds = userFriends.stream().map(UserFriend::getId).collect(Collectors.toList());
        userFriendDao.removeByIds(friendRecordIds);
        //禁用房间
        roomService.disableFriendRoom(Arrays.asList(uid, friendUid));
    }

    @Override
    public CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request) {
        CursorPageBaseResp<UserFriend> friendPage = userFriendDao.getFriendPage(uid, request);
        if (CollectionUtils.isEmpty(friendPage.getList())) {
            return CursorPageBaseResp.empty();
        }
        List<Long> friendUids = friendPage.getList()
                .stream().map(UserFriend::getFriendUid)
                .collect(Collectors.toList());
        List<User> userList = userDao.getFriendList(friendUids);
        return CursorPageBaseResp.init(friendPage, FriendAdapter.buildFriend(friendPage.getList(), userList));
    }

    @Override
    public List<Long> friendUids(Long uid) {
        return userFriendDao.getFriendUids(uid);
    }

    private void createFriend(Long uid, Long targetUid) {
        UserFriend userFriend1 = new UserFriend();
        userFriend1.setUid(uid);
        userFriend1.setFriendUid(targetUid);
        UserFriend userFriend2 = new UserFriend();
        userFriend2.setUid(targetUid);
        userFriend2.setFriendUid(uid);
        userFriendDao.saveBatch(Lists.newArrayList(userFriend1, userFriend2));
    }

}
