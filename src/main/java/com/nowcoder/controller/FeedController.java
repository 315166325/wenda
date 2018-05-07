package com.nowcoder.controller;

import com.alibaba.fastjson.JSON;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 * Created by nowcoder on 2016/7/15.
 */
@Controller
public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    LoginTicketService loginTicketService;
    @Autowired
    ActivityValueService activityValueService;

    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getPushTimelineKey(localUserId), 0, 50);
        List<Feed> feeds = new ArrayList<Feed>();
        for (String feedId : feedIds) {
            Feed feed = feedService.getById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0) {
            // 关注的人
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 50);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    @RequestMapping(path = {"/pullandpushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPullAndPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<Feed> feeds;
        //先判断当前用户是推还是拉,redis中有推的键，则为推
        String pushKey = RedisKeyUtil.getPushTimelineKey(localUserId);//要推的地方
        if (jedisAdapter.exists(pushKey)) {
//            List<Integer> activeUsers=activityValueService.getActivityValueBetween(Double.MAX_VALUE,ActivityValueService.MIN_ACTIVITY_VALUE);//top5个最活跃的user
//            List<Integer> onlineUsers=loginTicketService.getCurrentOnlineUsers();
//            if(activeUsers.contains(localUserId) || onlineUsers.contains(localUserId)){
            List<String> feed_push = jedisAdapter.lrange(pushKey, 0, -1);//推给我的数据
            feeds = parseToFeedList(feed_push);
//            }

        } else {
            List<String> feed_pull = new ArrayList<String>();
            if (localUserId != 0) {
                // 关注的人
                List<Integer> followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
                followees.stream().forEach(followee -> {
                    String pullKey = RedisKeyUtil.getPullTimelineKey(followee);
                    feed_pull.addAll(jedisAdapter.lrange(pullKey, 0, -1));
                });
            }
            feeds = parseToFeedList(feed_pull);
        }
        feeds.sort(new Comparator<Feed>() {
            @Override
            public int compare(Feed o1, Feed o2) {
                return o2.getId() - o1.getId();
            }
        });
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    private List<Feed> parseToFeedList(List<String> feedStringList) {
        List<Feed> feedList = new ArrayList<Feed>(feedStringList.size());
        feedStringList.stream().forEach(feedString -> {
            Feed feed = JSON.parseObject(feedString, Feed.class);
            feedList.add(feed);
        });
        return feedList;
    }
}
