package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.User;
import com.nowcoder.service.ActivityValueService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserScoreHandle implements EventHandler {
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    UserService userService;
    @Autowired
    ActivityValueService activityValueService;

    @Override
    public void doHandle(EventModel model) {
        if (model.getType()==EventType.LOGIN) {
            String firstLoginKey = RedisKeyUtil.getFirstLoginKey();
            if (jedisAdapter.sadd(firstLoginKey, String.valueOf(model.getActorId())) == 1) {
                if (jedisAdapter.ttl(firstLoginKey) == -1) {//需要设置过期时间
                    long dayMilliseconds=24*60*60*1000;
                    Date now=new Date();
                    long expiretime = now.getTime()-(now.getTime()+TimeZone.getDefault().getRawOffset())%dayMilliseconds+dayMilliseconds;
                    jedisAdapter.pexpireAt(firstLoginKey, expiretime);
                }
                User user = userService.getUser(model.getActorId());
                user.setScore(user.getScore() + 10);
                userService.updateScore(user);
            }
        }

        if (model.getType()==EventType.ADD_QUESTION){//提问题加5活跃度
            User user = userService.getUser(model.getActorId());
            user.setScore(user.getScore() + 5);
            userService.updateScore(user);
        }

        if (model.getType()==EventType.COMMENT){//回答问题加5活跃度
            User user = userService.getUser(model.getActorId());
            user.setScore(user.getScore() + 5);
            userService.updateScore(user);
        }

        if (model.getType()==EventType.LIKE){//被点赞 加2活跃度
            User user=userService.getUser(model.getEntityOwnerId());
            user.setScore(user.getScore() + 2);
            userService.updateScore(user);
        }

        //score发生变化，排行榜变动，加入redis中的set中


    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN,EventType.ADD_QUESTION,EventType.COMMENT,EventType.LIKE);
    }
}
