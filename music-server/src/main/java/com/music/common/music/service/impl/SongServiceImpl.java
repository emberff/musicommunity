package com.music.common.music.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.dao.*;
import com.music.common.music.domain.entity.*;
import com.music.common.music.domain.enums.PowerTypeEnum;
import com.music.common.music.domain.enums.SongTypeEnum;
import com.music.common.music.domain.vo.reponse.SimpleSongListResp;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.domain.vo.request.PlaylistAddReq;
import com.music.common.music.domain.vo.request.PlaylistUpdateReq;
import com.music.common.music.domain.vo.request.SongAddReq;
import com.music.common.music.domain.vo.request.SongUpdateReq;
import com.music.common.music.service.IPlaylistService;
import com.music.common.music.service.ISongService;
import com.music.common.music.service.adapter.PlaylistAdapter;
import com.music.common.music.service.adapter.SongAdapter;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import com.music.common.websocket.domain.vo.resp.WSBaseResp;
import com.music.common.websocket.domain.vo.resp.WSFriendApply;
import com.music.common.websocket.domain.vo.resp.WSMessage;
import com.music.common.websocket.domain.vo.resp.WSSongAdd;
import com.music.common.websocket.service.WebSocketService;
import com.music.common.websocket.service.adapter.WSAdapter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SongServiceImpl implements ISongService {
    @Autowired
    private SongDao songDao;
    @Autowired
    private PlaylistDao playlistDao;
    @Autowired
    private SingerDao singerDao;
    @Autowired
    private SongRecDao songRecDao;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserFollowDao userFollowDao;


    @Override
    public SongDetailResp getSongDetail(Long id) {
        Song song = songDao.getById(id);
        AssertUtil.isNotEmpty(song, "未查询到歌曲!");
        // 专辑信息
        Playlist playlist = playlistDao.getById(song.getPlaylistId());
        // 歌手信息
        Singer singer = singerDao.getById(song.getSingerId());
        return SongAdapter.buildSongDetail(song, playlist, singer);
    }

    @Override
    public PageBaseResp<SimpleSongListResp> getSongPage(PageBaseReq req) {
        IPage<Song> pageResult = songDao.getPage(req.plusPage()); // 这个 pageResult 包含 total、records 等
        List<Song> songs = pageResult.getRecords();
        return SongAdapter.buildSimpleSongListRespPage(songs, pageResult); // 传分页对象进去

    }

    @Override
    public Boolean saveSong(SongAddReq req) {
        //若非歌手, 直接将其添加至歌手表
        Long uid = RequestHolder.get().getUid();
        User user = userDao.getById(uid);
        Singer one = singerDao.lambdaQuery().eq(Singer::getUid, uid).one();
        if (one == null) {
            one = Singer.builder()
                    .uid(uid)
                    .name(user.getName())
                    .avatar(user.getAvatar())
                    .build();
            singerDao.save(one);
        }

        Song song = new Song();
        if (req.getType().equals(SongTypeEnum.JAMENDO_API.getValue())) {
            AssertUtil.isNotEmpty(req.getId(), "API歌曲id不可为空!");
            song.setCover("https://usercontent.jamendo.com?type=album&id=" +req.getId() + "&width=300&trackid=2245166");
        }
        song.setId(req.getId());
        song.setName(req.getName());
        song.setUrl(req.getUrl());
        song.setSingerId(one.getId());
        song.setType(SongTypeEnum.UPLOAD.getValue());
        songDao.save(song);


        List<UserFollow> followers = userFollowDao.lambdaQuery().eq(UserFollow::getSingerId, one.getId()).list();
        log.info("该歌手被 {} 个用户关注", followers.size());
        for (UserFollow userFollow : followers) {
            try {
                WSBaseResp<WSSongAdd> wsSongAddWSBaseResp = WSAdapter.buildSongAdd(new WSSongAdd(uid, song.getName()));
                webSocketService.sendToUid(wsSongAddWSBaseResp, userFollow.getUserId());
            } catch (Exception e) {
                log.error("推送歌曲消息失败, userId={}", userFollow.getUserId(), e);
            }
        }


        return true;
    }

    @Override
    public Boolean updateSong(SongUpdateReq req) {
        AssertUtil.isNotEmpty(req.getId(), "id不可为空!");
        Song song = songDao.getById(req.getId());
        song.setName(req.getName());
        song.setSingerId(req.getSingerId());
        song.setCover(req.getCover());
        song.setUrl(req.getUrl());
        return songDao.updateById(song);
    }

    @Override
    public PageBaseResp<SimpleSongListResp> getSongRecPage(Long uid, PageBaseReq req) {
        // 1. 查询用户推荐的歌曲ID列表
        List<Long> songIds = songRecDao.getByUid(uid);
        if (CollUtil.isEmpty(songIds)) {
            return PageBaseResp.empty(); // 返回空分页
        }

        // 2. 对songIds分页
        int pageNo = req.getPageNo();
        int pageSize = req.getPageSize();
        int fromIndex = (pageNo - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, songIds.size());

        if (fromIndex >= songIds.size()) {
            return PageBaseResp.empty(); // 页码超出范围
        }

        List<Long> pagedSongIds = songIds.subList(fromIndex, toIndex);

        // 3. 查询对应的歌曲详情
        List<Song> songList = songDao.selectBatchIds(pagedSongIds);

        // 4. 转换为 SimpleSongListResp
        List<SimpleSongListResp> respList = songList.stream()
                .map(song -> {
                    SimpleSongListResp resp = new SimpleSongListResp();
                    resp.setId(song.getId());
                    resp.setName(song.getName());
                    resp.setCover(song.getCover());
                    resp.setUrl(song.getUrl());
                    return resp;
                })
                .collect(Collectors.toList());

        // 5. 返回分页响应
        return PageBaseResp.init(pageNo, pageSize, (long) songIds.size(), respList);
    }



}
