package com.nowcoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.listener.Event;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.LoginTicketService;
import com.nowcoder.util.CollectionUtils;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.swing.text.Style;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class FollowServiceTests {
    @Autowired
    FollowService followService;
    @Autowired
    UserDAO userDAO;
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    FeedService feedService;
    @Autowired
    ApplicationContext applicationContext;

    @Before
    public void setUp() {
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    public void testFollow() {
//        for (int id=0;id<=13;id++){
//            User user=userDAO.selectById(id);
//            followService.follow(id,EntityType.ENTITY_USER,12);
//        }

//        String key=RedisKeyUtil.getFollowerKey(EntityType.ENTITY_QUESTION,6931);
//        Question question=questionDAO.getById(6931);
//        String json=JSONObject.toJSONString(question);
//        System.out.println("问题"+json);
//        for(int id=1;id<=14;id++){
//            User user=userDAO.selectById(id);
//            user.setScore(user.getScore()+(10*id));
//            userDAO.updateScore(user);
//        }
//        for(int id=21;id>=1;id--){
//            long time=new Date().getTime()-id*4*60*60*1000;
//            loginTicketDAO.updateCreatedTime(id,new Date(time));
//        }
//        long now = System.currentTimeMillis() -1000*60 * 60 * 18;
//        long daySecond = 1000*60 * 60 * 24;
//        long dayTime = now - (now + 8 * 1000*3600) % daySecond;
//        Date date=new Date(dayTime);
//        System.out.println(date);
//         苏曼  小小酥。。。。小小、、
//        Feed feed =feedService.getById(10);
//        String feedStr=JSON.toJSONString(feed);
//        JSONObject jsonObject=JSONObject.parseObject(feedStr);
//        Date date=new Date();//当前北京时间
//        date.setTime(date.getTime()-10*3600000);
//        Date dateGMT=new Date(date.getTime()-8*3600000);//GMT中央时区时间
//        Date zeroGMT=new Date(dateGMT.getTime()-dateGMT.getTime()%(24*3600000));
//        long time=date.getTime()-date.getTime()%(24*3600000);

//        List<Integer> userids=loginTicketDAO.getCurrentOnlineUsers(new Date(),1);
//
//        System.out.println(userids);

//        applicationContext.publishEvent(new Event("我的被监听的Event"));

//        String key="test";
//        jedisAdapter.del(key);
//        String[] values={"test1","test2","test3"};
////        jedisAdapter.lpush(key,values);
//        System.out.println(jedisAdapter.exists(key));
//        String[] values={"test1","test2","test3"};
//        List<String> list= Arrays.asList(values);
//        String[] s=
//        System.out.println(Arrays.toString(s));

//        ArrayList<String> list3= new ArrayList<>();
//        list3.add("a");
//        list3.add("b");
//        list3.add("c");
//        list3.add("d");
//        String[] arr1 = list3.toArray(new String[0]);
//        String[] arr3 = list3.toArray(new String[10]);
//        System.out.println(Arrays.toString(arr1));
//        System.out.println(Arrays.toString(arr3));
//        List<Integer> userIds=loginTicketDAO.getCurrentOnlineUsers(new Date(),1);
//        System.out.println(userIds);
        jedisAdapter.getPool().getResource();
        jedisAdapter.getPool().getResource();
        while (true);
        Collections.singletonList(0);

    }

}
