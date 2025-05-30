package com.music.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.music.common.common.domain.vo.req.CursorPageBaseReq;
import com.music.common.common.domain.vo.resp.CursorPageBaseResp;
import com.music.common.common.utils.CursorUtils;
import com.music.common.user.domain.entity.UserFriend;
import com.music.common.user.mapper.UserFriendMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户联系人表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2023-07-16
 */
@Service
public class UserFriendDao extends ServiceImpl<UserFriendMapper, UserFriend> {

    public List<UserFriend> getByFriends(Long uid, List<Long> uidList) {
        return lambdaQuery().eq(UserFriend::getUid, uid)
                .in(UserFriend::getFriendUid, uidList)
                .list();
    }

    public UserFriend getByFriend(Long uid, Long targetUid) {
        return lambdaQuery().eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, targetUid)
                .one();
    }

    public CursorPageBaseResp<UserFriend> getFriendPage(Long uid, CursorPageBaseReq cursorPageBaseReq) {
        return CursorUtils.getCursorPageByMysql(this, cursorPageBaseReq,
                wrapper -> wrapper.eq(UserFriend::getUid, uid), UserFriend::getId);
    }

    public List<UserFriend> getUserFriend(Long uid, Long friendUid) {
        return lambdaQuery()
                .eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, friendUid)
                .or()
                .eq(UserFriend::getFriendUid, uid)
                .eq(UserFriend::getUid, friendUid)
                .select(UserFriend::getId)
                .list();
    }

    public List<Long> getFriendUids(Long uid){
        return lambdaQuery()
                .eq(UserFriend::getDeleteStatus, 0)
                .eq(UserFriend::getUid, uid)
                .select(UserFriend::getFriendUid)
                .list()
                .stream()
                .map(UserFriend::getFriendUid)
                .collect(Collectors.toList());
    }



}
