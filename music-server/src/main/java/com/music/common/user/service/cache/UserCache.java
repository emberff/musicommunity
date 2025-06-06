package com.music.common.user.service.cache;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import com.music.common.common.constant.RedisKey;
import com.music.common.common.domain.vo.req.CursorPageBaseReq;
import com.music.common.common.domain.vo.resp.CursorPageBaseResp;
import com.music.common.common.utils.CursorUtils;
import com.music.common.common.utils.RedisUtils;
import com.music.common.music.dao.PowerDao;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserCache {
    @Autowired
    private PowerDao powerDao;

    public Long getOnlineNum() {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        return RedisUtils.zCard(onlineKey);
    }

    public Long getOfflineNum() {
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);
        return RedisUtils.zCard(offlineKey);
    }

    //移除用户
    public void remove(Long uid) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);
        //移除离线表
        RedisUtils.zRemove(offlineKey, uid);
        //移除上线表
        RedisUtils.zRemove(onlineKey, uid);
    }

    //用户上线
    public void online(Long uid, Date optTime) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);
        //移除离线表
        RedisUtils.zRemove(offlineKey, uid);
        //更新上线表
        RedisUtils.zAdd(onlineKey, uid, optTime.getTime());
    }

    //获取用户上线列表
    public List<Long> getOnlineUidList() {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        Set<String> strings = RedisUtils.zAll(onlineKey);
        return strings.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    public boolean isOnline(Long uid) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        return RedisUtils.zIsMember(onlineKey, uid);
    }

    //用户下线
    public void offline(Long uid, Date optTime) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);
        //移除上线线表
        RedisUtils.zRemove(onlineKey, uid);
        //更新上线表
        RedisUtils.zAdd(offlineKey, uid, optTime.getTime());
    }

    public CursorPageBaseResp<Pair<Long, Double>> getOnlineCursorPage(CursorPageBaseReq pageBaseReq) {
        return CursorUtils.getCursorPageByRedis(pageBaseReq, RedisKey.getKey(RedisKey.ONLINE_UID_ZET), Long::parseLong);
    }

    public CursorPageBaseResp<Pair<Long, Double>> getOfflineCursorPage(CursorPageBaseReq pageBaseReq) {
        return CursorUtils.getCursorPageByRedis(pageBaseReq, RedisKey.getKey(RedisKey.OFFLINE_UID_ZET), Long::parseLong);
    }

    public List<Long> getUserModifyTime(List<Long> uidList) {
        List<String> keys = uidList.stream().map(uid -> RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid)).collect(Collectors.toList());
        return RedisUtils.mget(keys, Long.class);
    }

    public void refreshUserModifyTime(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid);
        RedisUtils.set(key, new Date().getTime());
    }

//    /**
//     * 获取用户信息，盘路缓存模式
//     */
//    public User getUserInfo(Long uid) {//todo 后期做二级缓存
//        return getUserInfoBatch(Collections.singleton(uid)).get(uid);
//    }

//    /**
//     * 获取用户信息，盘路缓存模式
//     */
//    public Map<Long, User> getUserInfoBatch(Set<Long> uids) {
//        //批量组装key
//        List<String> keys = uids.stream().map(a -> RedisKey.getKey(RedisKey.USER_INFO_STRING, a)).collect(Collectors.toList());
//        //批量get
//        List<User> mget = RedisUtils.mget(keys, User.class);
//        Map<Long, User> map = mget.stream().filter(Objects::nonNull).collect(Collectors.toMap(User::getId, Function.identity()));
//        //发现差集——还需要load更新的uid
//        List<Long> needLoadUidList = uids.stream().filter(a -> !map.containsKey(a)).collect(Collectors.toList());
//        if (CollUtil.isNotEmpty(needLoadUidList)) {
//            //批量load
//            List<User> needLoadUserList = userDao.listByIds(needLoadUidList);
//            Map<String, User> redisMap = needLoadUserList.stream().collect(Collectors.toMap(a -> RedisKey.getKey(RedisKey.USER_INFO_STRING, a.getId()), Function.identity()));
//            RedisUtils.mset(redisMap, 5 * 60);
//            //加载回redis
//            map.putAll(needLoadUserList.stream().collect(Collectors.toMap(User::getId, Function.identity())));
//        }
//        return map;
//    }

//    public void userInfoChange(Long uid) {
//        delUserInfo(uid);
//        //删除UserSummaryCache，前端下次懒加载的时候可以获取到最新的数据
//        userSummaryCache.delete(uid);
//        refreshUserModifyTime(uid);
//    }

    public void delUserInfo(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_INFO_STRING, uid);
        RedisUtils.del(key);
    }

}
