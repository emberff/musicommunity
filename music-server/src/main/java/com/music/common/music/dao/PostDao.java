package com.music.common.music.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.music.common.common.domain.enums.NormalOrNoEnum;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.music.domain.entity.Post;
import com.music.common.music.mapper.PostMapper;
import com.music.common.music.service.IPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 发布作品表-参考tiktok 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-20
 */
@Service
public class PostDao extends ServiceImpl<PostMapper, Post>{

    public IPage<Post> getPage(PageBaseReq req) {
        return lambdaQuery().eq(Post::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .page(req.plusPage());
    }
}
