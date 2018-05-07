package com.nowcoder.redisCache;

import com.nowcoder.model.User;
import com.nowcoder.service.UserService;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 没必要作为缓存，因为一直在变动，要么直接用redis存储。
 */

@Service
public class UserScoreCache implements InitializingBean {
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    UserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        String userScoreCacheKey=RedisKeyUtil.getUserScoreCacheKey();
        List<User> userList=userService.getAll();
        userList.stream().forEach(user->jedisAdapter.hset(userScoreCacheKey,String.valueOf(user.getId()),String.valueOf(user.getScore())));
    }
}
