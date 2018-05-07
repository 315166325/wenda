package com.nowcoder.controller;

import com.alibaba.fastjson.JSON;
import com.nowcoder.aspect.AOPclass;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.ParseObjectToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by nowcoder on 2016/7/15.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    FeedService feedService;
    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    AOPclass aoPclass;

    @Autowired
    ActivityValueService activityValueService;

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
        logger.info("index首页 方法执行中...");
        model.addAttribute("vos", getQuestions(0, 0, 50));
        return "index";
    }

    @RequestMapping(path = {"/suman"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession) {
//        logger.info("VISIT HOME");
//        List<User> userList=userService.getIdArange(5,10);
//        List<Feed> feeds=new ArrayList<>();
//        userList.stream().forEach(a->feeds.addAll(feedService.getFeedsByUser(a.getId())));
//        Set<Feed> feedSet=new HashSet<>();
//        feeds.stream().forEach(feed -> feedSet.add(feed));
//        System.out.println(feeds);
//        System.out.println(feedSet);
        aoPclass.A();
        System.out.println(".....................");
        aoPclass.B();
        return "suman";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));

        User user = userService.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        vo.set("activityValue",activityValueService.getActivityValue(user));
        model.addAttribute("profileUser", vo);
        return "profile";
    }
}
