package com.music.common.music.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.domain.enums.NormalOrNoEnum;
import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.dao.*;
import com.music.common.music.domain.entity.*;
import com.music.common.music.domain.enums.SongTypeEnum;
import com.music.common.music.domain.vo.reponse.PostPageResp;
import com.music.common.music.domain.vo.reponse.PostResp;
import com.music.common.music.domain.vo.reponse.SimpleSongListResp;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.domain.vo.request.PostAddReq;
import com.music.common.music.domain.vo.request.PostUpdateReq;
import com.music.common.music.domain.vo.request.SongAddReq;
import com.music.common.music.domain.vo.request.SongUpdateReq;
import com.music.common.music.service.IPostContentService;
import com.music.common.music.service.IPostService;
import com.music.common.music.service.ISongService;
import com.music.common.music.service.adapter.SongAdapter;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    private PostDao postDao;
    @Autowired
    private PostContentDao postContentDao;
    @Autowired
    private IPostContentService postContentService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PostLikeDao postLikeDao;
    @Autowired
    private PostCommentDao postCommentDao;

    @Override
    @Transactional
    public Boolean addPost(PostAddReq req) {
        Long uid = RequestHolder.get().getUid();
        User user = userDao.getById(uid);
        Post post = Post.builder()
                .userId(uid)
                .userName(user.getName())
                .userAvatar(user.getAvatar())
                .description(req.getDescription())
                .likeCount(0)
                .commentCount(0)
                .shareCount(0)
                .status(NormalOrNoEnum.NORMAL.getStatus())
                .build();
        postDao.save(post);

        List<String> urls = req.getUrls();
        for (int i = 0; i < urls.size(); i++) {
            String postContent = urls.get(i);
            PostContent content = PostContent.builder()
                    .postId(post.getId())
                    .url(postContent)
                    .sort(i)
                    .build();
            postContentDao.save(content);
        }

        return true;
    }

    @Override
    public PostResp getPostDetail(Long id) {
        Post post = postDao.getById(id);
        AssertUtil.equal(post.getStatus(), NormalOrNoEnum.NORMAL.getStatus(), "发布内容已被隐藏! ");
        List<String> urls = postContentDao.getByPostId(id);
        PostResp postResp = new PostResp();
        postResp.setUid(post.getUserId());
        postResp.setName(post.getUserName());
        postResp.setAvatar(post.getUserAvatar());
        postResp.setDescription(post.getDescription());
        postResp.setUrls(urls);
        postResp.setLikeCount(post.getLikeCount());
        postResp.setCommentCount(post.getCommentCount());
        postResp.setShareCount(post.getShareCount());
        postResp.setCreateTime(post.getCreateTime());
        return postResp;
    }

    @Override
    public PageBaseResp<PostPageResp> getPostPage(PageBaseReq req) {
        IPage<Post> page = postDao.getPage(req);
        List<Post> records = page.getRecords();
        List<PostPageResp> postPageResps = BeanUtil.copyToList(records, PostPageResp.class);
        for (PostPageResp postPageResp : postPageResps) {
            PostContent postContent = postContentDao.lambdaQuery()
                    .eq(PostContent::getPostId, postPageResp.getId())
                    .eq(PostContent::getSort, 0).one();
            postPageResp.setUrl(postContent.getUrl());
            //点赞数
            Integer likeCnt = postLikeDao.lambdaQuery().eq(PostLike::getPostId, postPageResp.getId()).count();
            postPageResp.setLikeCnt(likeCnt);
            //评论数
            Integer commentCnt = postCommentDao.lambdaQuery().eq(PostComment::getPostId, postPageResp.getId())
                    .eq(PostComment::getStatus, NormalOrNoEnum.NORMAL.getStatus()).count();
            postPageResp.setCommentCnt(commentCnt);
        }
        return PageBaseResp.init(page, postPageResps);
    }

    @Override
    public Boolean updatePost(PostUpdateReq req) {
        // 校验帖子 ID 是否为空
        AssertUtil.isNotEmpty(req.getId(), "帖子id不能为空");

        // 查询帖子是否存在
        Post post = postDao.getById(req.getId());
        AssertUtil.isNotEmpty(post, "未查询到帖子!");

        // 只更新非 null 字段
        if (req.getDescription() != null) {
            post.setDescription(req.getDescription());
        }
        if (req.getStatus() != null) {
            post.setStatus(req.getStatus());
        }
        postDao.updateById(post);
        //帖子内容更新: 先删除旧数据, 再插入新数据
        if (CollectionUtil.isNotEmpty(req.getUrls())) {
            postContentDao.lambdaUpdate()
                    .eq(PostContent::getPostId, req.getId())
                    .remove();
            for (int i = 0; i < req.getUrls().size(); i++) {
                String postContent = req.getUrls().get(i);
                PostContent content = PostContent.builder()
                        .postId(post.getId())
                        .url(postContent)
                        .sort(i)
                        .build();
                postContentDao.save(content);
            }
        }
        return true;
    }

    @Override
    public Boolean likePost(IdReqVO idReqVO) {
        Post post = postDao.getById(idReqVO.getId());
        AssertUtil.isNotEmpty(post, "帖子不存在");

        Long uid = RequestHolder.get().getUid();
        PostLike postLike = postLikeDao.lambdaQuery()
                .eq(PostLike::getPostId, idReqVO.getId())
                .eq(PostLike::getUserId, uid)
                .one();

        Integer currentLikeCount = post.getLikeCount() != null ? post.getLikeCount() : 0;

        if (postLike == null) {
            // 创建新的点赞记录
            postLike = new PostLike();
            postLike.setPostId(idReqVO.getId());
            postLike.setUserId(uid);
            postLike.setLike(NormalOrNoEnum.NORMAL.getStatus());

            // 点赞数+1
            post.setLikeCount(currentLikeCount + 1);
        } else {
            // 切换点赞状态
            if (postLike.getLike().equals(NormalOrNoEnum.NORMAL.getStatus())) {
                // 从已点赞切换到取消点赞
                postLike.setLike(NormalOrNoEnum.NOT_NORMAL.getStatus());
                // 点赞数-1（确保不小于0）
                post.setLikeCount(Math.max(0, currentLikeCount - 1));
            } else {
                // 从取消点赞切换到已点赞
                postLike.setLike(NormalOrNoEnum.NORMAL.getStatus());
                // 点赞数+1
                post.setLikeCount(currentLikeCount + 1);
            }
        }

        // 保存更新
        postDao.updateById(post);
        postLikeDao.saveOrUpdate(postLike);

        return true;
    }

    @Override
    public PageBaseResp<PostPageResp> getLikePostPage(PageBaseReq req) {
        Long uid = RequestHolder.get().getUid();

        // 第一步：查询当前用户点赞的记录分页
        IPage<PostLike> likePage = postLikeDao.lambdaQuery()
                .eq(PostLike::getUserId, uid)
                .eq(PostLike::getLike, NormalOrNoEnum.NORMAL.getStatus())
                .page(req.plusPage());

        List<Long> postIds = likePage.getRecords().stream()
                .map(PostLike::getPostId)
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(postIds)) {
            return PageBaseResp.empty();
        }

        // 第二步：根据 postIds 批量查询帖子信息（分页中已包含记录限制）
        List<Post> postList = postDao.lambdaQuery()
                .in(Post::getId, postIds)
                .list();

        List<PostPageResp> postPageResps = BeanUtil.copyToList(postList, PostPageResp.class);

        // 第三步：补全 PostPageResp 的附加信息（内容、点赞数、评论数）
        for (PostPageResp postPageResp : postPageResps) {
            PostContent postContent = postContentDao.lambdaQuery()
                    .eq(PostContent::getPostId, postPageResp.getId())
                    .eq(PostContent::getSort, 0)
                    .one();

            if (postContent != null) {
                postPageResp.setUrl(postContent.getUrl());
            }

            Integer likeCnt = postLikeDao.lambdaQuery()
                    .eq(PostLike::getPostId, postPageResp.getId())
                    .count();
            postPageResp.setLikeCnt(likeCnt);

            Integer commentCnt = postCommentDao.lambdaQuery()
                    .eq(PostComment::getPostId, postPageResp.getId())
                    .eq(PostComment::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                    .count();
            postPageResp.setCommentCnt(commentCnt);
        }

        return PageBaseResp.init(likePage, postPageResps);
    }



}
