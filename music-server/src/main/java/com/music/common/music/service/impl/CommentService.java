package com.music.common.music.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.domain.enums.NormalOrNoEnum;
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
import java.util.stream.Stream;

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
                .content(req.getContent())
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
        if (comment.getStatus().equals(YesOrNoEnum.YES.getStatus())) {
            comment.setStatus(YesOrNoEnum.NO.getStatus());
        } else {
            comment.setStatus(YesOrNoEnum.YES.getStatus());
        }
        commentDao.save(comment);
    }

    @Override
    public PageBaseResp<CommentPageRespVO> pageComment(CommentPageReq req) {
        // 1. 分页获取 parentId = 0 的根评论
        Page<Comment> page = commentDao.lambdaQuery()
                .eq(Comment::getSongId, req.getSongId())
                .eq(Comment::getParentId, 0L)
                // 如果Comment确实有status字段，保留下面这行，否则移除
                .eq(ObjectUtil.isNotNull(NormalOrNoEnum.NORMAL), Comment::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .orderByDesc(Comment::getCreateTime)
                .page(new Page<>(req.getPageNo(), req.getPageSize()));

        // 打印调试信息
//        log.info("查询到的根评论数量: {}", page.getRecords().size());

        List<Comment> rootComments = page.getRecords();
        if (rootComments.isEmpty()) {
            return PageBaseResp.empty();
        }

        List<Long> rootIds = rootComments.stream().map(Comment::getId).collect(Collectors.toList());

        // 2. 查询子评论
        List<Comment> childComments = commentDao.lambdaQuery()
                .in(Comment::getParentId, rootIds)
                // 如果Comment确实有status字段，保留下面这行，否则移除
                .eq(ObjectUtil.isNotNull(NormalOrNoEnum.NORMAL), Comment::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .list();

        // 打印调试信息
//        log.info("查询到的子评论数量: {}", childComments.size());

        // 3. 合并评论用户ID
        Set<Long> userIds = Stream.concat(
                rootComments.stream().map(Comment::getUserId),
                childComments.stream().map(Comment::getUserId)
        ).filter(Objects::nonNull).collect(Collectors.toSet());

        // 4. 获取用户信息
        List<User> users = userDao.listByIds(userIds);
        Map<Long, User> userInfoMap = users.stream()
                .collect(Collectors.toMap(User::getId, Function.identity(), (e1, e2) -> e1));

        // 打印调试信息
//        log.info("查询到的用户数量: {}, 用户ID数量: {}", userInfoMap.size(), userIds.size());

        // 5. 构建评论VO
        Map<Long, CommentPageRespVO> voMap = new HashMap<>();

        for (Comment comment : rootComments) {
            CommentPageRespVO vo = new CommentPageRespVO();
            BeanUtil.copyProperties(comment, vo);

            // 确保所有重要字段都被设置
            vo.setId(comment.getId());
            vo.setParentId(comment.getParentId());
            vo.setContent(comment.getContent());

            User user = userInfoMap.get(comment.getUserId());
            if (user != null) {
                vo.setNickName(user.getName());
                vo.setAvatar(user.getAvatar());
            }
            vo.setChildren(new ArrayList<>());
            voMap.put(comment.getId(), vo);
        }

        for (Comment comment : childComments) {
            CommentPageRespVO vo = new CommentPageRespVO();
            BeanUtil.copyProperties(comment, vo);

            // 确保所有重要字段都被设置
            vo.setId(comment.getId());
            vo.setParentId(comment.getParentId());
            vo.setContent(comment.getContent());

            User user = userInfoMap.get(comment.getUserId());
            if (user != null) {
                vo.setNickName(user.getName());
                vo.setAvatar(user.getAvatar());
            }
            vo.setChildren(new ArrayList<>());
            voMap.put(comment.getId(), vo);

            // 添加到父评论
            CommentPageRespVO parentVo = voMap.get(comment.getParentId());
            if (parentVo != null) {
                parentVo.getChildren().add(vo);
            }
        }

        // 6. 返回根评论列表
        List<CommentPageRespVO> resultList = rootComments.stream()
                .map(comment -> voMap.get(comment.getId()))
                .collect(Collectors.toList());

        return PageBaseResp.init(page, resultList);
    }

}
