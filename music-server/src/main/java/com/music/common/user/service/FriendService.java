package com.music.common.user.service;


import com.music.common.common.domain.vo.req.CursorPageBaseReq;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.CursorPageBaseResp;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.user.domain.vo.request.friend.FriendApplyReq;
import com.music.common.user.domain.vo.request.friend.FriendApproveReq;
import com.music.common.user.domain.vo.request.friend.FriendCheckReq;
import com.music.common.user.domain.vo.request.friend.InvitedFriendReq;
import com.music.common.user.domain.vo.response.friend.*;

import java.util.List;

/**
 * @author : limeng
 * @description : 好友
 * @date : 2023/07/19
 */
public interface FriendService {

    /**
     * 检查
     * 检查是否是自己好友
     *
     * @param request 请求
     * @param uid     uid
     * @return {@link FriendCheckResp}
     */
    FriendCheckResp check(Long uid, FriendCheckReq request);

    /**
     * 应用
     * 申请好友
     *
     * @param request 请求
     * @param uid     uid
     */
    void apply(Long uid, FriendApplyReq request);

    /**
     * 分页查询好友申请
     *
     * @param request 请求
     * @return {@link PageBaseResp}<{@link FriendApplyResp}>
     */
    PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq request);

    /**
     * 申请未读数
     *
     * @return {@link FriendUnreadResp}
     */
    FriendUnreadResp unread(Long uid);

    /**
     * 同意好友申请
     *
     * @param uid     uid
     * @param request 请求
     */
    void applyApprove(Long uid, FriendApproveReq request);

    /**
     * 删除好友
     *
     * @param uid       uid
     * @param friendUid 朋友uid
     */
    void deleteFriend(Long uid, Long friendUid);

    CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request);

    List<Long> friendUids(Long uid);

    CursorPageBaseResp<InviteFriendResp> invitedFriendList(Long uid, InvitedFriendReq req);
}
