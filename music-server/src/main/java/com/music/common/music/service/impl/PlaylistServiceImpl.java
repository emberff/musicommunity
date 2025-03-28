package com.music.common.music.service.impl;

import com.music.common.common.exception.BusinessException;
import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.dao.PlaylistDao;
import com.music.common.music.dao.PlaylistSongDao;
import com.music.common.music.dao.PowerDao;
import com.music.common.music.dao.UserPlaylistDao;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.entity.PlaylistSong;
import com.music.common.music.domain.entity.Power;
import com.music.common.music.domain.entity.UserPlaylist;
import com.music.common.music.domain.enums.IsPublicEnum;
import com.music.common.music.domain.enums.PowerTypeEnum;
import com.music.common.music.domain.vo.reponse.PlaylistDetailResp;
import com.music.common.music.domain.vo.request.AddSongToPlaylistReq;
import com.music.common.music.domain.vo.request.PlaylistAddReq;
import com.music.common.music.domain.vo.request.PlaylistUpdateReq;
import com.music.common.music.service.IPlaylistService;
import com.music.common.music.service.IPlaylistSongService;
import com.music.common.music.service.adapter.PlaylistAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void addPlaylist(PlaylistAddReq req) {
        Long uid = RequestHolder.get().getUid();

        Playlist playlist = Playlist.builder()
                .name(req.getName())
                .cover(req.getCover())
                .isPublic(req.getIsPublic())
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
        AssertUtil.isEmpty(playlist, "未查询到歌单信息!");
        AssertUtil.isTrue(validateMngPower(RequestHolder.get().getUid(), req.getId()), "无修改权限!");
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
            if (!validateMngPower(RequestHolder.get().getUid(), playlistId)) {
                throw new BusinessException("不可查看歌单!");
            }
        }
        List<PlaylistSong> simpleSongList =  playlistSongDao.getSimpleSongListByPlaylistId(playlistId);
        return PlaylistAdapter.buildPlaylistDetail(playlist, simpleSongList);
    }

    @Override
    public void addSongToPlaylist(AddSongToPlaylistReq req) {
        Playlist playlist = playlistDao.getById(req.getPlaylistId());
        AssertUtil.isNotEmpty(playlist, "未查询到歌单信息!");
        Boolean checkPower = validateMngPower(req.getPlaylistId(), RequestHolder.get().getUid());
        AssertUtil.isTrue(checkPower, "无修改歌单信息权限!");
        playlistSongService.addSongsToPlaylist(req.getPlaylistId(), req.getSongIds());
//        playlistSongDao.addSongToPlaylist(req.getPlaylistId(), req.getSongIds());
    }


    // 校验角色管理权限(创建者 和 管理员)
    private Boolean validateMngPower(Long playlistId, Long uid) {
        List<Integer> powerList = new ArrayList<>();
        powerList.add(PowerTypeEnum.CREATOR.getValue());
        powerList.add(PowerTypeEnum.ADMIN.getValue());
        return powerDao.checkPower(uid, playlistId, powerList);
    }

}
