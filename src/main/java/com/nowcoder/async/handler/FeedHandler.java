package com.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.CollectionUtils;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by nowcoder on 2016/7/30.
 */
@Component
public class FeedHandler implements EventHandler,InitializingBean{
    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    QuestionService questionService;
    @Autowired
    ActivityValueService activityValueService;
    @Autowired
    LoginTicketService loginTicketService;

    private Set<Integer> redisPushUserIds;
    @Override
    public void afterPropertiesSet() throws Exception {
        String pushKeyPattern=RedisKeyUtil.getPushTimelineKeyPattern();
        Set<String> pushKeys=jedisAdapter.keys(pushKeyPattern+"*");//正则
        redisPushUserIds = new HashSet<Integer>();
        if(pushKeys == null){
            return;
        }else {
            pushKeys.forEach(key->{
                redisPushUserIds.add(Integer.parseInt(key.substring(pushKeyPattern.length()),key.length()));
            });
        }
    }


    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<String, String>();
        // 触发用户是通用的
        User actor = userService.getUser(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if (model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION) ||
                (model.getType() == EventType.ADD_QUESTION)) {
            Question question = questionService.getById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
//        // 为了测试，把model的userId随机一下
//        Random r = new Random();
//        model.setActorId(1+r.nextInt(10));

        // 构造一个新鲜事
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            // 不支持的feed
            return;
        }

        feedService.addFeed(feed);//把产生的feed存入到mysql中

        String feedJson=JSONObject.toJSONString(feed);
        jedisAdapter.lpush(RedisKeyUtil.getPullTimelineKey(model.getActorId()),feedJson);//拉模式数据
        //活跃和在线用户推，推到他们对应的redis的list。两点：活跃用户且是粉丝
        List<Integer> activeUsers=activityValueService.getActivityValueBetween(Double.MAX_VALUE,ActivityValueService.MIN_ACTIVITY_VALUE);//top5个最活跃的user
        List<Integer> onlineUsers=loginTicketService.getCurrentOnlineUsers();
        //对当前的push判断，是否已经应该删除了
        redisPushUserIds.forEach(pushUserId->{
            if (!activeUsers.contains(pushUserId) && !onlineUsers.contains(pushUserId)){
                jedisAdapter.del(RedisKeyUtil.getPushTimelineKey(pushUserId));
            }
        });
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), 0, -1);
        followers.stream().forEach(follower->{
            if (activeUsers.contains(follower) || onlineUsers.contains(follower)){//在线 活跃 用户采用推模式
                //用一个set保存redis中的pushkey
                redisPushUserIds.add(follower);
                String pushKey=RedisKeyUtil.getPushTimelineKey(follower);//要推的地方
                String pullKey=RedisKeyUtil.getPullTimelineKey(model.getActorId());//从发起者那里拉取
                if(!jedisAdapter.exists(pushKey)){//如果不存在，全部拉过来。
                    List<String> pullFeeds=jedisAdapter.lrange(pullKey,0,-1);
                    jedisAdapter.lpush(pushKey,pullFeeds.toArray(new String[pullFeeds.size()]));
                }else {
                    jedisAdapter.lpush(pushKey, feedJson);
                }
            }
        });


    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW, EventType.ADD_QUESTION});//关注对象评论或者关注的时候会触发dohandle
    }


}
