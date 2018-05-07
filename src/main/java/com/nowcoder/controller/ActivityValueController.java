package com.nowcoder.controller;

import com.nowcoder.model.User;
import com.nowcoder.service.ActivityValueService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Tuple;

import java.util.*;

@Controller
public class ActivityValueController {
    @Autowired
    UserService userService;
    @Autowired
    ActivityValueService activityValueService;
    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(path = {"/activityValueRank"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String activityValueRank(Model model) {
        String activityValueRank=RedisKeyUtil.getActivityValueRank();
        Set<Tuple> idset=jedisAdapter.zrevrangeWithScores(activityValueRank,0,-1);//所有的userid 从高到低活跃排序
        StringBuilder sb=new StringBuilder();
        idset.stream().forEach(tuple -> sb.append(tuple.getElement()).append("活跃度：").append(tuple.getScore()).append(">>>"));
        return sb.toString();




//        List<User> userList = userService.getIdArange(1, 14);//全部user
//        TreeMap<User, Double> activityValueMap = new TreeMap<User, Double>(new Comparator<User>() {
//            @Override
//            public int compare(User o1, User o2) {
//                double activityValue1 = activityValueService.getActivityValue(o1);
//                double activityValue2 = activityValueService.getActivityValue(o2);
//                return activityValue1 < activityValue2 ? 1 : ((activityValue1 == activityValue2) ? 0 : -1);
//            }
//        });
//        userList.stream().forEach(user -> activityValueMap.put(user,activityValueService.getActivityValue(user)));
//        StringBuilder sb=new StringBuilder();
//        int rank=0;
//        for(Map.Entry<User,Double> entry:activityValueMap.entrySet()){
//            ++rank;
//            sb.append("第"+rank+"名").append(" ").append(entry.getKey().getName()).append("活跃值").append(entry.getValue()).append("\n");
//        }
//        return sb.toString();

   }
}
