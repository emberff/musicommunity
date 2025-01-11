package com.music.common.common.constant;

/**
 * Redis Key的管理
 */
public class RedisKey {
    private static final String BASE_KEY = "community:music";

    /**
     * 用户token的key
     */
    public static final String USER_TOKEN_STRING = "userToken:uid_%d";

    public static String getKey(String key, Object... o) {
        return BASE_KEY + String.format(key, o);
    }
}
