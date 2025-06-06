package com.music.common.user.service.adapter;


import com.music.common.chat.domain.vo.response.ChatMemberListResp;
import com.music.common.user.domain.entity.User;
import com.music.common.user.domain.entity.UserApply;
import com.music.common.user.domain.entity.UserFriend;
import com.music.common.user.domain.vo.request.friend.FriendApplyReq;
import com.music.common.user.domain.vo.response.friend.FriendApplyResp;
import com.music.common.user.domain.vo.response.friend.FriendResp;
import com.music.common.user.domain.vo.response.friend.InviteFriendResp;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.music.common.user.domain.enums.ApplyReadStatusEnum.UNREAD;
import static com.music.common.user.domain.enums.ApplyStatusEnum.WAIT_APPROVAL;
import static com.music.common.user.domain.enums.ApplyTypeEnum.ADD_FRIEND;


/**
 * Description: 好友适配器
 * Author: <a href="https://github.com/emberff">pf</a>
 * Date: 2023-07-22
 */
public class FriendAdapter {


    public static UserApply buildFriendApply(Long uid, FriendApplyReq request) {
        UserApply userApplyNew = new UserApply();
        userApplyNew.setUid(uid);
        userApplyNew.setMsg(request.getMsg());
        userApplyNew.setType(ADD_FRIEND.getCode());
        userApplyNew.setTargetId(request.getTargetUid());
        userApplyNew.setStatus(WAIT_APPROVAL.getCode());
        userApplyNew.setReadStatus(UNREAD.getCode());
        return userApplyNew;
    }

    public static List<FriendApplyResp> buildFriendApplyList(List<UserApply> records, Map<Long, User> users) {
        return records.stream().map(userApply -> {
            FriendApplyResp friendApplyResp = new FriendApplyResp();
            friendApplyResp.setUid(userApply.getUid());
            User user = users.get(userApply.getUid());
            friendApplyResp.setName(user.getName()); // 设置名称
            friendApplyResp.setSex(user.getSex());
            friendApplyResp.setAvatar(user.getAvatar());
            friendApplyResp.setType(userApply.getType());
            friendApplyResp.setApplyId(userApply.getId());
            friendApplyResp.setMsg(userApply.getMsg());
            friendApplyResp.setStatus(userApply.getStatus());
            friendApplyResp.setCreateTime(userApply.getCreateTime());
            return friendApplyResp;
        }).collect(Collectors.toList());
    }


    public static List<FriendResp> buildFriend(List<UserFriend> list, List<User> userList) {
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        return list.stream().map(userFriend -> {
            FriendResp resp = new FriendResp();
            resp.setUid(userFriend.getFriendUid());
            User user = userMap.get(userFriend.getFriendUid());
            if (Objects.nonNull(user)) {
                resp.setSign(user.getSign());
                resp.setActiveStatus(user.getActiveStatus());
                resp.setName(user.getName());
                resp.setAvatar(user.getAvatar());
                resp.setSex(user.getSex());
            }
            return resp;
        }).collect(Collectors.toList());
    }

    public static List<InviteFriendResp> buildInvitedFriend(List<UserFriend> list, List<User> userList, List<ChatMemberListResp> memberList) {
        Map<Long, User> userMap = userList.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // 提取群成员的 uid，用于判断是否已被邀请
        Set<Long> memberUidSet = memberList.stream()
                .map(ChatMemberListResp::getUid)
                .collect(Collectors.toSet());

        return list.stream().map(userFriend -> {
            InviteFriendResp resp = new InviteFriendResp();
            Long friendUid = userFriend.getFriendUid();
            resp.setUid(friendUid);

            // 设置是否已被邀请：在群成员列表中为已邀请（1），否则为未邀请（0）
            resp.setIsInvited(memberUidSet.contains(friendUid) ? 1 : 0);

            User user = userMap.get(friendUid);
            if (user != null) {
                resp.setSign(user.getSign());
                resp.setActiveStatus(user.getActiveStatus());
                resp.setName(user.getName());
                resp.setAvatar(user.getAvatar());
                resp.setSex(user.getSex());
            }
            return resp;
        }).collect(Collectors.toList());
    }

}
