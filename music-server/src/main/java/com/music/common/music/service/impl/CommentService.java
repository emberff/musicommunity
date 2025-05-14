package com.music.common.music.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.domain.enums.YesOrNoEnum;
import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.dao.CommentDao;
import com.music.common.music.domain.entity.Comment;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.enums.PlayListTypeEnum;
import com.music.common.music.domain.vo.reponse.CommentPageRespVO;
import com.music.common.music.domain.vo.request.CommentAddReq;
import com.music.common.music.domain.vo.request.CommentPageReq;
import com.music.common.music.service.ICommentService;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import com.music.common.user.domain.vo.response.user.UserInfoResp;
import com.music.common.user.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentService implements ICommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    @Override
    public void addComment(CommentAddReq req) {
        Comment comment = Comment.builder()
                .songId(req.getSongId())
                .parentId(req.getParentId() == null ? 0 : req.getParentId())//未传则为0
                .userId(RequestHolder.get().getUid())
                .cotent(req.getCotent())
                .status(YesOrNoEnum.YES.getStatus())
                .build();
        commentDao.save(comment);
    }

    @Override
    public void updateComment(IdReqVO req) {
        Comment comment = commentDao.getById(req.getId());
        Long uid = RequestHolder.get().getUid();
        if (userService.isAdmin(uid)) {
            AssertUtil.equal(comment.getUserId(), uid, "无权限修改!");
        }
        comment.setStatus(YesOrNoEnum.NO.getStatus());
        commentDao.save(comment);
    }

    @Override
    public PageBaseResp<CommentPageRespVO> pageComment(CommentPageReq req) {
        // 1. 分页获取 parentId = 0 的根评论
        Page<Comment> page = commentDao.lambdaQuery()
                .eq(Comment::getSongId, req.getSongId())
                .eq(Comment::getParentId, 0L)
                .orderByDesc(Comment::getCreateTime)
                .page(new Page<>(req.getPageNo(), req.getPageSize()));

        List<Comment> rootComments = page.getRecords();
        if (rootComments.isEmpty()) {
            return PageBaseResp.empty();
        }

        List<Long> rootIds = rootComments.stream().map(Comment::getId).collect(Collectors.toList());

        // 2. 查询子评论
        List<Comment> childComments = commentDao.lambdaQuery()
                .in(Comment::getParentId, rootIds)
                .list();

        // 3. 合并所有评论用户ID
        List<Comment> allComments = new ArrayList<>();
        allComments.addAll(rootComments);
        allComments.addAll(childComments);

        Set<Long> userIds = allComments.stream().map(Comment::getUserId).collect(Collectors.toSet());

        // 4. 获取用户信息
        Map<Long, User> userInfoMap = userIds.stream()
                .collect(Collectors.toMap(Function.identity(), userDao::getById));

        // 5. 转换为 VO 并填充用户信息
        Map<Long, CommentPageRespVO> voMap = allComments.stream().map(comment -> {
            CommentPageRespVO vo = BeanUtil.copyProperties(comment, CommentPageRespVO.class);
            User userInfo = userInfoMap.get(comment.getUserId());
            if (userInfo != null) {
                vo.setNickName(userInfo.getName());
                vo.setAvatar(userInfo.getAvatar());
            }
            vo.setChildren(new ArrayList<>());
            return vo;
        }).collect(Collectors.toMap(CommentPageRespVO::getId, Function.identity()));

        // 6. 组装嵌套结构
        for (Comment child : childComments) {
            CommentPageRespVO parent = voMap.get(child.getParentId());
            if (parent != null) {
                parent.getChildren().add(voMap.get(child.getId()));
            }
        }

        // 7. 返回根评论分页响应
        List<CommentPageRespVO> resultList = rootComments.stream()
                .map(c -> voMap.get(c.getId()))
                .collect(Collectors.toList());

        return PageBaseResp.init(page, resultList);
    }

}
