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

import java.util.Arrays;
import java.util.List;

@Component
public class ActivityValueRankHandle implements EventHandler {
    @Autowired
    UserService userService;
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    ActivityValueService activityValueService;
    @Override
    public void doHandle(EventModel model) {
        User user=null;
        if (model.getType()==EventType.LIKE){
            user=userService.getUser(model.getEntityOwnerId());
        }else {
            user=userService.getUser(model.getActorId());
        }
        String activityValueRank=RedisKeyUtil.getActivityValueRank();
        jedisAdapter.zadd(activityValueRank,activityValueService.getActivityValue(user),String.valueOf(user.getId()));
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN,EventType.ADD_QUESTION,EventType.COMMENT,EventType.LIKE);
    }
}
