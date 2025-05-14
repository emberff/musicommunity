package com.music.common.music.dao;

import com.music.common.music.domain.entity.Comment;
import com.music.common.music.mapper.CommentMapper;
import com.music.common.music.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-14
 */
@Service
public class CommentDao extends ServiceImpl<CommentMapper, Comment> {

}
