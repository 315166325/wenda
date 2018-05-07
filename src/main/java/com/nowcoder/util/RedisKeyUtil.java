package com.nowcoder.util;

/**
 * Created by nowcoder on 2016/7/30.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    // 获取粉丝
    private static String BIZ_FOLLOWER = "FOLLOWER";
    // 关注对象
    private static String BIZ_FOLLOWEE = "FOLLOWEE";
    private static String BIZ_TIMELINE_PUSH = "TIMELINE_PUSH";
    //每天中登录过的用户id放在redis的set中
    private static String BIZ_FIRST_LOGIN_EVERYDAY = "FIRST_LOGIN_EVERYDAY";
    //userScoreCacheKey
    private static String USER_SCORE_CACHE_KEY = "USER_SCORE_CACHE_KEY";
    //ActivityValueRank
    private static String ACTIVITY_VALUE_RANK = "ACTIVITY_VALUE_RANK";
    private static String BIZ_TIMELINE_PULL = "TIMELINE_PULL";

    public static String getActivityValueRank() {
        return ACTIVITY_VALUE_RANK;
    }

    public static String getUserScoreCacheKey() {
        return USER_SCORE_CACHE_KEY;
    }

    public static String getFirstLoginKey() {
        return BIZ_FIRST_LOGIN_EVERYDAY;
    }

    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }

    // 某个实体的粉丝key  3 12的粉丝 BIZ_FOLLOWER:3:12
    public static String getFollowerKey(int entityType, int entityId) {

        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    // 每个用户对某类实体的关注key
    public static String getFolloweeKey(int userId, int entityType) {
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    public static String getPushTimelineKey(int userId) {
        return BIZ_TIMELINE_PUSH + SPLIT + String.valueOf(userId);
    }

    public static String getPushTimelineKeyPattern() {
        return BIZ_TIMELINE_PUSH + SPLIT ;
    }

    public static String getPullTimelineKey(int userId) {
        return BIZ_TIMELINE_PULL + SPLIT + String.valueOf(userId);
    }
}
