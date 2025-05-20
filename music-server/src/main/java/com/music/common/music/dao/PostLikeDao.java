package com.music.common.music.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.music.common.music.domain.entity.PostLike;
import com.music.common.music.mapper.PostLikeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-20
 */
@Service
public class PostLikeDao extends ServiceImpl<PostLikeMapper, PostLike> implements IService<PostLike> {

}
