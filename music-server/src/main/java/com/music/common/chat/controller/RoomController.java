package com.music.common.chat.controller;


import com.music.common.chat.domain.vo.request.ChatMessageMemberReq;
import com.music.common.chat.domain.vo.request.GroupAddReq;
import com.music.common.chat.domain.vo.request.admin.AdminAddReq;
import com.music.common.chat.domain.vo.request.admin.AdminRevokeReq;
import com.music.common.chat.domain.vo.request.member.MemberAddReq;
import com.music.common.chat.domain.vo.request.member.MemberDelReq;
import com.music.common.chat.domain.vo.request.member.MemberExitReq;
import com.music.common.chat.domain.vo.request.member.MemberReq;
import com.music.common.chat.domain.vo.response.ChatMemberListResp;
import com.music.common.chat.domain.vo.response.MemberResp;
import com.music.common.chat.service.RoomAppService;
import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.domain.vo.resp.CursorPageBaseResp;
import com.music.common.common.domain.vo.resp.IdRespVO;
import com.music.common.common.utils.RequestHolder;
import com.music.common.websocket.domain.vo.resp.ChatMemberResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 房间相关接口
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2023-03-19
 */
@RestController
@RequestMapping("/capi/room")
@Api(tags = "聊天室相关接口")
@Slf4j
public class RoomController {
    @Autowired
    private RoomAppService roomService;
//    @Autowired
//    private IGroupMemberService groupMemberService;

    @GetMapping("/public/group")
    @ApiOperation("群组详情")
    public ApiResult<MemberResp> groupDetail(@Valid IdReqVO request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(roomService.getGroupDetail(uid, request.getId()));
    }

    @GetMapping("/public/group/member/page")
    @ApiOperation("群成员列表")
    public ApiResult<CursorPageBaseResp<ChatMemberResp>> getMemberPage(@Valid MemberReq request) {
        return ApiResult.success(roomService.getMemberPage(request));
    }
    

//    @GetMapping("/group/member/list")
//    @ApiOperation("房间内的所有群成员列表-@专用")
//    public ApiResult<List<ChatMemberListResp>> getMemberList(@Valid ChatMessageMemberReq request) {
//        return ApiResult.success(roomService.getMemberList(request));
//    }

//    @DeleteMapping("/group/member")
//    @ApiOperation("移除成员")
//    public ApiResult<Void> delMember(@Valid @RequestBody MemberDelReq request) {
//        Long uid = RequestHolder.get().getUid();
//        roomService.delMember(uid, request);
//        return ApiResult.success();
//    }

//    @DeleteMapping("/group/member/exit")
//    @ApiOperation("退出群聊")
//    public ApiResult<Boolean> exitGroup(@Valid @RequestBody MemberExitReq request) {
//        Long uid = RequestHolder.get().getUid();
//        groupMemberService.exitGroup(uid, request);
//        return ApiResult.success();
//    }

    //TODO 新增和邀请与歌单管理模块集成
//    @PostMapping("/group")
//    @ApiOperation("新增群组")
//    public ApiResult<IdRespVO> addGroup(@Valid @RequestBody GroupAddReq request) {
//        Long uid = RequestHolder.get().getUid();
//        Long roomId = roomService.addGroup(uid, request);
//        return ApiResult.success(IdRespVO.id(roomId));
//    }

    @PostMapping("/group/member")
    @ApiOperation("邀请好友")
    public ApiResult<Void> addMember(@Valid @RequestBody MemberAddReq request) {
        Long uid = RequestHolder.get().getUid();
        roomService.addMember(uid, request);
        return ApiResult.success();
    }

//    @PutMapping("/group/admin")
//    @ApiOperation("添加管理员")
//    public ApiResult<Boolean> addAdmin(@Valid @RequestBody AdminAddReq request) {
//        Long uid = RequestHolder.get().getUid();
//        groupMemberService.addAdmin(uid, request);
//        return ApiResult.success();
//    }

//    @DeleteMapping("/group/admin")
//    @ApiOperation("撤销管理员")
//    public ApiResult<Boolean> revokeAdmin(@Valid @RequestBody AdminRevokeReq request) {
//        Long uid = RequestHolder.get().getUid();
//        groupMemberService.revokeAdmin(uid, request);
//        return ApiResult.success();
//    }
}
