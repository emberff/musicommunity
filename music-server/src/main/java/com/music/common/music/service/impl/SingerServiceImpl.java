package com.music.common.music.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.utils.AssertUtil;
import com.music.common.music.dao.PlaylistDao;
import com.music.common.music.dao.SingerDao;
import com.music.common.music.dao.SongDao;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.entity.Singer;
import com.music.common.music.domain.entity.Song;
import com.music.common.music.domain.vo.reponse.PlaylistPageResp;
import com.music.common.music.domain.vo.reponse.SingerDetailResp;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.service.ISingerService;
import com.music.common.music.service.ISongService;
import com.music.common.music.service.adapter.SingerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SingerServiceImpl implements ISingerService {
    @Autowired
    private SingerDao singerDao;

    @Override
    public SingerDetailResp getSingerDetail(Long id) {
        Singer singer = singerDao.getById(id);
        AssertUtil.isNotEmpty(singer, "未查询到歌手信息!");
        return SingerAdapter.buildSingerDetail(singer);
    }

    @Override
    public PageBaseResp<PlaylistPageResp> pageSinger(PageBaseReq req) {
        // 1. 获取分页结果
        Page<Singer> pageParam = req.plusPage();
        IPage<Singer> pageResult = singerDao.getPage(pageParam);

        // 2. 转换数据
        List<PlaylistPageResp> respList = pageResult.getRecords().stream()
                .map(singer -> BeanUtil.copyProperties(singer, PlaylistPageResp.class))
                .collect(Collectors.toList());

        // 3. 封装分页响应
        return PageBaseResp.init(pageResult, respList);
    }

}
