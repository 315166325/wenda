package com.nowcoder.service;

import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 活跃度
 */
@Service
public class ActivityValueService {
    @Autowired
    LoginTicketService loginTicketService;
    @Autowired
    JedisAdapter jedisAdapter;

    public static final double MIN_ACTIVITY_VALUE=150;

    public double getActivityValue(User user){
        LoginTicket loginTicket=loginTicketService.selectLatestByUserId(user.getId());
        if(loginTicket==null){
            return user.getScore();
        }
        long loginTime=loginTicket.getCreatedTime().getTime();
        long now=new Date().getTime();
        long dayMilliseconds=24*60*60*1000;
        long todayZeroTime=now-(now+TimeZone.getDefault().getRawOffset())%dayMilliseconds;
        int daysAgo= loginTime >= todayZeroTime? 0: (int) (1 + (todayZeroTime - loginTime) / (dayMilliseconds));
        return user.getScore()-10.0*Math.log(daysAgo+1);
    }

    public List<Integer> topActivityUserIds(int topk){
        String activityRankKey=RedisKeyUtil.getActivityValueRank();
        Set<String> idSet=jedisAdapter.zrevrange(activityRankKey,0,topk);
        List<Integer> idList=new ArrayList<Integer>(idSet.size());
        idSet.stream().forEach(idString->idList.add(Integer.parseInt(idString)));
        return idList;
    }

    public List<Integer> getActivityValueBetween(double max,double min){
        String activityRankKey=RedisKeyUtil.getActivityValueRank();
        Set<String> idSet=jedisAdapter.zrevrangeByScore(activityRankKey,max,min);
        List<Integer> idList=new ArrayList<Integer>(idSet.size());
        idSet.stream().forEach(idString->idList.add(Integer.parseInt(idString)));
        return idList;
    }
}
