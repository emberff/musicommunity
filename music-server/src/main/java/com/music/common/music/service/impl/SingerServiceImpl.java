package com.music.common.music.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.domain.enums.NormalOrNoEnum;
import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.exception.BusinessException;
import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.dao.*;
import com.music.common.music.domain.entity.*;
import com.music.common.music.domain.enums.IsPublicEnum;
import com.music.common.music.domain.enums.PowerTypeEnum;
import com.music.common.music.domain.vo.reponse.*;
import com.music.common.music.service.ISingerService;
import com.music.common.music.service.ISongService;
import com.music.common.music.service.adapter.PlaylistAdapter;
import com.music.common.music.service.adapter.SingerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SingerServiceImpl implements ISingerService {
    @Autowired
    private SingerDao singerDao;
    @Autowired
    private UserFollowDao userFollowDao;
    @Autowired
    private SongDao songDao;
    @Autowired
    private PlaylistDao playlistDao;
    @Autowired
    private PlaylistSongDao playlistSongDao;
    @Autowired
    private PowerDao powerDao;

    @Override
    public SingerDetailResp getSingerDetail(Long id) {
        Singer singer = singerDao.getById(id);
        Integer count = songDao.lambdaQuery()
                .eq(Song::getSingerId, id)
                .eq(Song::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .count();
        AssertUtil.isNotEmpty(singer, "未查询到歌手信息!");
        return SingerAdapter.buildSingerDetail(singer, count);
    }

    @Override
    public PageBaseResp<SingerPageResp> pageSinger(PageBaseReq req) {
        Page<Singer> pageParam = req.plusPage();
        Long uid = RequestHolder.get().getUid();
        IPage<Singer> pageResult = singerDao.getPage(pageParam);

        // 2. 转换数据
        Set<Long> followedSingerIds = userFollowDao.lambdaQuery()
                .eq(UserFollow::getUserId, uid)
                .list()
                .stream()
                .map(UserFollow::getSingerId)
                .collect(Collectors.toSet());

        // 2. 复制并设置 isFollowed 字段
        List<SingerPageResp> respList = pageResult.getRecords().stream()
                .map(singer -> {
                    SingerPageResp resp = BeanUtil.copyProperties(singer, SingerPageResp.class);
                    resp.setIsFollowed(followedSingerIds.contains(singer.getId()) ? 1 : 0);
                    return resp;
                })
                .collect(Collectors.toList());

        // 3. 封装分页响应
        return PageBaseResp.init(pageResult, respList);
    }

    @Override
    public Boolean followSinger(IdReqVO reqVO) {
        Long uid = RequestHolder.get().getUid();
        UserFollow userFollow = userFollowDao.getBySingerIdAndUid(reqVO.getId(), uid);
        Singer singer = singerDao.getById(reqVO.getId());
        if (userFollow == null) {
            // 没关注 -> 插入
            UserFollow newFollow = new UserFollow();
            newFollow.setSingerId(reqVO.getId());
            newFollow.setUserId(uid);
            userFollowDao.save(newFollow);

            singer.setFollowNum(singer.getFollowNum() + 1);
            singerDao.updateById(singer);
        } else {
            // 已关注 -> 删除
            userFollowDao.removeById(userFollow.getId());
            singer.setFollowNum(singer.getFollowNum() - 1);
            singerDao.updateById(singer);
        }
        return true;
    }

    @Override
    public List<PlaylistDetailResp> getSingerAlbum(Long singerId) {
        // 1. 查询该歌手的所有正常状态的歌曲
        List<Song> songList = songDao.lambdaQuery()
                .eq(Song::getSingerId, singerId)
                .eq(Song::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .list();

        // 2. 收集歌曲对应的专辑ID（去重）
        Set<Long> playlistIdSet = songList.stream()
                .map(Song::getPlaylistId) // 这里假设 Song 类中有 getAlbumId 方法
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(playlistIdSet)) {
            return null; // 无数据返回空分页
        }

        // 3. 获取当前用户 ID
        Long currentUid = RequestHolder.get().getUid();

        List<PlaylistDetailResp> resultList = new ArrayList<>();

        for (Long playlistId : playlistIdSet) {
            Playlist playlist = playlistDao.getById(playlistId);
            if (playlist == null) {
                continue; // 歌单不存在跳过
            }

//            // 4. 校验访问权限
//            if (!IsPublicEnum.IS_PUBLIC.getValue().equals(playlist.getIsPublic())) {
//                if (!validateMngPower(playlistId, currentUid)) {
//                    continue; // 无权限跳过
//                }
//            }

            // 5. 查询歌单内的歌曲信息
            List<PlaylistSong> playlistSongList = playlistSongDao.getSimpleSongListByPlaylistId(playlistId);
            List<SimpleSongListResp> simpleSongList = new ArrayList<>();
            for (PlaylistSong playlistSong : playlistSongList) {
                Song song = songDao.getById(playlistSong.getSongId());
                if (song != null) {
                    SimpleSongListResp resp = new SimpleSongListResp();
                    BeanUtil.copyProperties(song, resp);
                    simpleSongList.add(resp);
                }
            }

            // 6. 构建歌单详情
            PlaylistDetailResp detail = PlaylistAdapter.buildPlaylistDetail(playlist, simpleSongList);
            resultList.add(detail);
        }

        return resultList; // 如果需要分页，可在这里手动分页
    }

}
