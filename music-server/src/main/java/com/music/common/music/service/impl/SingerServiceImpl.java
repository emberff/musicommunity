package com.music.common.music.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.dao.PlaylistDao;
import com.music.common.music.dao.SingerDao;
import com.music.common.music.dao.SongDao;
import com.music.common.music.dao.UserFollowDao;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.entity.Singer;
import com.music.common.music.domain.entity.Song;
import com.music.common.music.domain.entity.UserFollow;
import com.music.common.music.domain.vo.reponse.PlaylistPageResp;
import com.music.common.music.domain.vo.reponse.SingerDetailResp;
import com.music.common.music.domain.vo.reponse.SingerPageResp;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.service.ISingerService;
import com.music.common.music.service.ISongService;
import com.music.common.music.service.adapter.SingerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SingerServiceImpl implements ISingerService {
    @Autowired
    private SingerDao singerDao;
    @Autowired
    private UserFollowDao userFollowDao;

    @Override
    public SingerDetailResp getSingerDetail(Long id) {
        Singer singer = singerDao.getById(id);
        AssertUtil.isNotEmpty(singer, "未查询到歌手信息!");
        return SingerAdapter.buildSingerDetail(singer);
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
            singerDao.save(singer);
        } else {
            // 已关注 -> 删除
            userFollowDao.removeById(userFollow.getId());
            singer.setFollowNum(singer.getFollowNum() - 1);
            singerDao.save(singer);
        }
        return true;
    }


}
