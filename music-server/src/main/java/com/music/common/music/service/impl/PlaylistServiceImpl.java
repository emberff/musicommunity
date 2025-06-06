package com.music.common.music.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.music.common.chat.domain.vo.request.GroupAddReq;
import com.music.common.chat.domain.vo.request.member.MemberAddReq;
import com.music.common.chat.service.RoomAppService;
import com.music.common.common.domain.enums.NormalOrNoEnum;
import com.music.common.common.domain.vo.req.IdListReqVO;
import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.exception.BusinessException;
import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.dao.*;
import com.music.common.music.domain.entity.*;
import com.music.common.music.domain.enums.IsPublicEnum;
import com.music.common.music.domain.enums.PlayListTypeEnum;
import com.music.common.music.domain.enums.PowerTypeEnum;
import com.music.common.music.domain.vo.reponse.PlaylistDetailResp;
import com.music.common.music.domain.vo.reponse.PlaylistPageResp;
import com.music.common.music.domain.vo.reponse.PlaylistSongPageResp;
import com.music.common.music.domain.vo.reponse.SimpleSongListResp;
import com.music.common.music.domain.vo.request.*;
import com.music.common.music.service.IPlaylistService;
import com.music.common.music.service.IPlaylistSongService;
import com.music.common.music.service.adapter.PlaylistAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlaylistServiceImpl implements IPlaylistService {
    @Autowired
    private PlaylistDao playlistDao;
    @Autowired
    private UserPlaylistDao userPlaylistDao;
    @Autowired
    private PowerDao powerDao;
    @Autowired
    private IPlaylistSongService playlistSongService;
    @Autowired
    private PlaylistSongDao playlistSongDao;
    @Autowired
    private SongDao songDao;
    @Autowired
    private SingerDao singerDao;
    @Autowired
    private RoomAppService roomService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPlaylist(PlaylistAddReq req) {
        Long uid = RequestHolder.get().getUid();
        //直接建立一个歌单对应的群聊房间
        GroupAddReq groupAddReq = new GroupAddReq();
        groupAddReq.setName(req.getName() + "管理群");
        groupAddReq.setAvatar(req.getCover());
        Long roomId = roomService.addGroup(uid, groupAddReq);
        Playlist playlist = Playlist.builder()
                .name(req.getName())
                .type(PlayListTypeEnum.USER_PLAYLIST.getValue())
                .cover(req.getCover())
                .isPublic(req.getIsPublic())
                .roomId(roomId)
                .plCreatorId(uid)
                .build();
        playlistDao.save(playlist);

        //角色关注歌单
        UserPlaylist userPlaylist = UserPlaylist.builder()
                .userId(uid)
                .playlistId(playlist.getId())
                .build();
        userPlaylistDao.save(userPlaylist);

        //权限
        Power power = Power.builder()
                .userId(uid)
                .playlistId(playlist.getId())
                .powerType(PowerTypeEnum.CREATOR.getValue())
                .build();
        powerDao.save(power);
    }

    @Override
    public void updatePlaylist(PlaylistUpdateReq req) {
        Playlist playlist = playlistDao.getById(req.getId());
        AssertUtil.isNotEmpty(playlist, "未查询到歌单信息!");
        AssertUtil.isTrue(validateMngPower(req.getId(), RequestHolder.get().getUid()), "无修改权限!");
        playlistDao.updateById(PlaylistAdapter.buildPlaylist(req));
    }

    @Override
    public void detetePlaylist(Long playlistId) {
        Playlist playlist = playlistDao.getById(playlistId);
        AssertUtil.isNotEmpty(playlist, "未查询到歌单信息!");
        //只有创建者可以删除歌单
        Boolean checkPower = powerDao.checkPower(RequestHolder.get().getUid(), playlistId, PowerTypeEnum.CREATOR.getValue());
        AssertUtil.isTrue(checkPower, "无权限删除!");
        playlistDao.disablePlaylist(playlistId);
    }

    @Override
    public PlaylistDetailResp getPlaylistDetail(Long playlistId) {
        Playlist playlist = playlistDao.getById(playlistId);
        AssertUtil.isNotEmpty(playlist, "未查询到歌单信息!");
        // 校验用户权限:
        // 1.歌单是否公开
        // 2.若非公开看用户是否为管理员
        if (!IsPublicEnum.IS_PUBLIC.getValue().equals(playlist.getIsPublic())) {
            if (!validateMngPower(playlistId, RequestHolder.get().getUid())) {
                throw new BusinessException("不可查看歌单!");
            }
        }
        List<PlaylistSong> playlistSongList =  playlistSongDao.getSimpleSongListByPlaylistId(playlistId);
        List<SimpleSongListResp> simpleSongList = new ArrayList<>();
        for (PlaylistSong playlistSong : playlistSongList) {
            Song song = songDao.getById(playlistSong.getSongId());
            SimpleSongListResp simpleSongListResp = new SimpleSongListResp();
            BeanUtil.copyProperties(song, simpleSongListResp);
            simpleSongList.add(simpleSongListResp);
        }
        UserPlaylist one = userPlaylistDao.lambdaQuery().eq(UserPlaylist::getUserId, RequestHolder.get().getUid())
                .eq(UserPlaylist::getPlaylistId, playlistId).one();
        Integer isFollowed = one == null ? 0 : 1;
        return PlaylistAdapter.buildPlaylistDetail(playlist, simpleSongList, isFollowed);
    }

    @Override
    public void addSongToPlaylist(SongToPlaylistReq req) {
        Playlist playlist = playlistDao.getById(req.getPlaylistId());
        AssertUtil.isNotEmpty(playlist, "未查询到歌单信息!");
        Boolean checkPower = validateMngPower(req.getPlaylistId(), RequestHolder.get().getUid());
        AssertUtil.isTrue(checkPower, "无修改歌单信息权限!");
        playlistSongService.addSongsToPlaylist(req.getPlaylistId(), req.getSongIds());
//        playlistSongDao.addSongToPlaylist(req.getPlaylistId(), req.getSongIds());
    }

    @Override
    public void deteteSongToPlaylist(SongToPlaylistReq req) {
        Playlist playlist = playlistDao.getById(req.getPlaylistId());
        AssertUtil.isNotEmpty(playlist, "未查询到歌单信息!");
        Boolean checkPower = validateMngPower(req.getPlaylistId(), RequestHolder.get().getUid());
        AssertUtil.isTrue(checkPower, "无修改歌单信息权限!");
        playlistSongService.deleteSongToPlaylist(req.getPlaylistId(), req.getSongIds());
    }

    @Override
    public PageBaseResp<PlaylistPageResp> pagePlaylist(Long uid, PageBaseReq req) {
        IPage<UserPlaylist> playlistIPage = userPlaylistDao.getPage(uid, req.plusPage());
        List<UserPlaylist> records = playlistIPage.getRecords();
        List<PlaylistPageResp> pageResp = new ArrayList<>();
        for(UserPlaylist record : records) {
            PlaylistPageResp resp = new PlaylistPageResp();
            Playlist playlist = playlistDao.getById(record.getPlaylistId());
            BeanUtil.copyProperties(playlist, resp);
            resp.setPlSongNum(playlistSongDao.getExistingSongIds(playlist.getId()).size());
            if (playlist.getStatus() == NormalOrNoEnum.NORMAL.getStatus()){
                pageResp.add(resp);
            }

        }

        return PageBaseResp.init(playlistIPage, pageResp);
    }

    @Override
    public PageBaseResp<PlaylistSongPageResp> pagePlaylistSong(PlaylistSongPageReq req) {
        Playlist playlist = playlistDao.getById(req.getPlaylistId());
        //非公开歌单, 且无对歌单的管理权限 => 不可查询
        boolean validate = validateMngPower(req.getPlaylistId(), RequestHolder.get().getUid()) || playlist.getIsPublic() == 1;
        AssertUtil.isTrue(validate, "无权限查看歌单信息");
        IPage<PlaylistSong> playlistSongIPage = playlistSongDao.getPage(req.getPlaylistId(), req.plusPage());
        List<PlaylistSongPageResp> pageResps = new ArrayList<>();
        for (PlaylistSong record : playlistSongIPage.getRecords()) {
            Song song = songDao.getById(record.getSongId());
            Singer singer = singerDao.getById(song.getSingerId());
            PlaylistSongPageResp resp = new PlaylistSongPageResp();
            BeanUtil.copyProperties(song, resp);
            resp.setSingerName(singer.getName());
            pageResps.add(resp);
        }
        return PageBaseResp.init(playlistSongIPage, pageResps);
    }

    @Override
    public PageBaseResp<PlaylistPageResp> pagePlaylist2(PageBaseReq req) {
        // 获取分页 Playlist 数据
        IPage<Playlist> playlistIPage = playlistDao.getPage(req.plusPage());

        // 将 Playlist 列表转换为 PlaylistPageResp 列表
        List<PlaylistPageResp> pageRespList = playlistIPage.getRecords().stream()
                .map(playlist -> {
                    PlaylistPageResp resp = new PlaylistPageResp();
                    BeanUtils.copyProperties(playlist, resp); // Apache Commons BeanUtils 或 Spring BeanUtils
                    return resp;
                })
                .collect(Collectors.toList());

        // 使用转换后的列表初始化分页响应
        return PageBaseResp.init(playlistIPage, pageRespList);
    }



    @Override
    public Boolean followPlaylist(IdReqVO reqVO) {
        Playlist playlist = playlistDao.getById(reqVO.getId());
        playlist.setPlFollowNumber(playlist.getPlFollowNumber() + 1);
        playlistDao.updateById(playlist);

        Long uid = RequestHolder.get().getUid();
        UserPlaylist userPlaylist = new UserPlaylist();
        userPlaylist.setUserId(uid);
        userPlaylist.setPlaylistId(reqVO.getId());
        return userPlaylistDao.save(userPlaylist);
    }

    @Override
    public PageBaseResp<PlaylistPageResp> pageRecPlaylist(Long uid, PageBaseReq req) {
        return null;
    }

    @Override
    public Boolean inviteFriend(PlaylistInviteReq reqVO) {
        Playlist playlist = playlistDao.getById(reqVO.getPlaylistId());
        AssertUtil.isNotEmpty(playlist, "未查到对应歌单!");

        followPlaylistByInvited(reqVO.getIdList(), reqVO.getPlaylistId());
        // 加入群聊
        MemberAddReq memberAddReq = new MemberAddReq();
        memberAddReq.setRoomId(playlist.getRoomId());
        memberAddReq.setUidList(reqVO.getIdList());
        Long uid = RequestHolder.get().getUid();
        AssertUtil.isTrue(validateMngPower(reqVO.getPlaylistId(), uid), "无权限!");

        roomService.addMember(uid, memberAddReq);

        //歌单权限
        for (Long userId : reqVO.getIdList()) {
            Power power = Power.builder()
                    .playlistId(reqVO.getPlaylistId())
                    .userId(userId)
                    .powerType(PowerTypeEnum.ADMIN.getValue())
                    .build();
            powerDao.save(power);
        }
        return true;
    }

    @Override
    public List<Playlist> getManageList() {
        Long uid = RequestHolder.get().getUid();
        List<Integer> powerList = new ArrayList<>();
        powerList.add(PowerTypeEnum.ADMIN.getValue());
        powerList.add(PowerTypeEnum.CREATOR.getValue());
        List<Power> list = powerDao.lambdaQuery().eq(Power::getUserId, uid)
                .in(Power::getPowerType, powerList)
                .list();

        List<Playlist> resps = new ArrayList<>();
        for (Power power : list) {
            Playlist byId = playlistDao.getById(power.getPlaylistId());
            resps.add(byId);
        }
        return resps;
    }

    @Override
    public PageBaseResp<Playlist> getFollowPlaylist(PageBaseReq req) {
        Long uid = RequestHolder.get().getUid();
        List<Playlist> list = new ArrayList<>();
        IPage<UserPlaylist> page = userPlaylistDao.lambdaQuery().eq(UserPlaylist::getUserId, uid).page(req.plusPage());
        for (UserPlaylist userPlaylist : page.getRecords()) {
            Playlist playlist = playlistDao.getById(userPlaylist.getPlaylistId());
            if (!Objects.equals(playlist.getPlCreatorId(), uid)) {
                list.add(playlist);
            }
        }
        return PageBaseResp.init(page, list);
    }

    /**
     * 将歌单加入被邀请好友的关注列表
     * @param uids
     * @param playlistId
     * @return
     */
    public Boolean followPlaylistByInvited(List<Long> uids, Long playlistId) {
        Playlist playlist = playlistDao.getById(playlistId);
        playlist.setPlFollowNumber(playlist.getPlFollowNumber() + 1);
        playlistDao.updateById(playlist);

        for (Long uid : uids) {
            UserPlaylist userPlaylist = new UserPlaylist();
            userPlaylist.setUserId(uid);
            userPlaylist.setPlaylistId(playlistId);
            userPlaylistDao.save(userPlaylist);
        }
        return true;
    }
    
    // 校验角色管理权限(创建者 和 管理员)
    private Boolean validateMngPower(Long playlistId, Long uid) {
        List<Integer> powerList = new ArrayList<>();
        powerList.add(PowerTypeEnum.CREATOR.getValue());
        powerList.add(PowerTypeEnum.ADMIN.getValue());
        return powerDao.checkPower(uid, playlistId, powerList);
    }

}
