package com.music.common.music.dao;

import com.music.common.music.domain.entity.PostContent;
import com.music.common.music.mapper.PostContentMapper;
import com.music.common.music.service.IPostContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户自制发布内容表: 1对多 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-20
 */
@Service
public class PostContentDao extends ServiceImpl<PostContentMapper, PostContent> implements IPostContentService {

    public List<String> getByPostId(Long id) {
        List<PostContent> contents = lambdaQuery().eq(PostContent::getPostId, id).orderByAsc(PostContent::getSort).list();
        return contents.stream().map(PostContent::getUrl).collect(Collectors.toList());
    }
}
