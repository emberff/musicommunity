package com.music.common.music.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.domain.enums.NormalOrNoEnum;
import com.music.common.common.domain.enums.YesOrNoEnum;
import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.dao.CommentDao;
import com.music.common.music.dao.PostCommentDao;
import com.music.common.music.dao.PostDao;
import com.music.common.music.domain.entity.Comment;
import com.music.common.music.domain.entity.Post;
import com.music.common.music.domain.entity.PostComment;
import com.music.common.music.domain.vo.reponse.CommentPageRespVO;
import com.music.common.music.domain.vo.reponse.PostCommentPageRespVO;
import com.music.common.music.domain.vo.request.CommentPageReq;
import com.music.common.music.domain.vo.request.PostCommentAddReq;
import com.music.common.music.domain.vo.request.PostCommentPageReq;
import com.music.common.music.service.IPostCommentService;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import com.music.common.user.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class PostCommentServiceImpl implements IPostCommentService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PostCommentDao postCommentDao;
    @Autowired
    private PostDao postDao;

    @Override
    @Transactional
    public Boolean addPostCpmment(PostCommentAddReq req) {
        //评论数+1
        Post post = postDao.getById(req.getPostId());
        post.setCommentCount(post.getCommentCount() + 1);
        postDao.updateById(post);
        PostComment postComment = PostComment.builder()
                .postId(req.getPostId())
                .parentId(req.getParentId() == null ? 0 : req.getParentId())//未传则为0
                .userId(RequestHolder.get().getUid())
                .content(req.getContent())
                .status(YesOrNoEnum.YES.getStatus())
                .build();
        postCommentDao.save(postComment);
        return true;
    }

    @Override
    public void updateComment(IdReqVO req) {
        PostComment postComment = postCommentDao.getById(req.getId());
        Long uid = RequestHolder.get().getUid();
        if (userService.isAdmin(uid)) {
            AssertUtil.equal(postComment.getUserId(), uid, "无权限修改!");
        }
        if (postComment.getStatus().equals(YesOrNoEnum.YES.getStatus())) {
            postComment.setStatus(YesOrNoEnum.NO.getStatus());
        } else {
            postComment.setStatus(YesOrNoEnum.YES.getStatus());
        }
        postCommentDao.save(postComment);
    }

    @Override
    public PageBaseResp<PostCommentPageRespVO> pagePostComment(PostCommentPageReq req) {
        // 1. 分页获取 parentId = 0 的根评论，并且只查询状态为NORMAL的评论
        Page<PostComment> page = postCommentDao.lambdaQuery()
                .eq(PostComment::getPostId, req.getPostId())
                .eq(PostComment::getParentId, 0L)
                .eq(PostComment::getStatus, NormalOrNoEnum.NORMAL.getStatus())  // 只查询正常状态的评论
                .orderByDesc(PostComment::getCreateTime)
                .page(new Page<>(req.getPageNo(), req.getPageSize()));

        List<PostComment> rootComments = page.getRecords();
        if (rootComments.isEmpty()) {
            return PageBaseResp.empty();
        }

        List<Long> rootIds = rootComments.stream().map(PostComment::getId).collect(Collectors.toList());

        // 2. 查询所有子评论 (不限制层级)，并且只查询状态为NORMAL的评论
        List<PostComment> allChildComments = postCommentDao.lambdaQuery()
                .eq(PostComment::getPostId, req.getPostId())
                .ne(PostComment::getParentId, 0L)  // 所有非根评论
                .eq(PostComment::getStatus, NormalOrNoEnum.NORMAL.getStatus())  // 只查询正常状态的评论
                .orderByDesc(PostComment::getCreateTime)
                .list();

        // 3. 合并所有评论用户ID
        Set<Long> userIds = Stream.concat(
                rootComments.stream().map(PostComment::getUserId),
                allChildComments.stream().map(PostComment::getUserId)
        ).collect(Collectors.toSet());

        // 4. 获取用户信息
        Map<Long, User> userInfoMap = userDao.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity(), (e1, e2) -> e1));

        // 5. 构建所有评论的VO映射
        Map<Long, PostCommentPageRespVO> voMap = new HashMap<>();

        // 处理所有评论，确保每个评论都有children集合
        List<PostComment> allComments = new ArrayList<>();
        allComments.addAll(rootComments);
        allComments.addAll(allChildComments);

        for (PostComment comment : allComments) {
            PostCommentPageRespVO vo = new PostCommentPageRespVO();
            BeanUtil.copyProperties(comment, vo);
            // 明确设置content字段
            vo.setContent(comment.getContent());

            User user = userInfoMap.get(comment.getUserId());
            if (user != null) {
                vo.setNickName(user.getName());
                vo.setAvatar(user.getAvatar());
            }
            vo.setChildren(new ArrayList<>());
            voMap.put(comment.getId(), vo);
        }

        // 6. 构建评论树结构
        for (PostComment child : allChildComments) {
            PostCommentPageRespVO childVo = voMap.get(child.getId());
            PostCommentPageRespVO parentVo = voMap.get(child.getParentId());

            if (parentVo != null && childVo != null) {
                parentVo.getChildren().add(childVo);
            }
        }

        // 7. 返回根评论列表
        List<PostCommentPageRespVO> resultList = rootComments.stream()
                .map(comment -> voMap.get(comment.getId()))
                .collect(Collectors.toList());

        return PageBaseResp.init(page, resultList);
    }

}
