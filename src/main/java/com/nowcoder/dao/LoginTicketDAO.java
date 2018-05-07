package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = "login_ticket";
    String USER_ID=" user_id ";
    String INSERT_FIELDS = " user_id, expired, status, ticket,created_time  ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{expired},#{status},#{ticket},#{createdTime})"})
    int addTicket(LoginTicket ticket);

    @Select({"select ",USER_ID, "from (select ", USER_ID, " from ", TABLE_NAME, " where expired > #{now} and status = #{status} order by id desc limit 999999) t"
            ," group by ",USER_ID
    })
    List<Integer> getCurrentOnlineUsers(@Param("now") Date now, @Param("status") int status);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id=#{userId} order by created_time desc limit 1 "})
    LoginTicket selectLatestByUserId(int userId);

    @Update({"update ", TABLE_NAME, " set created_time=#{date} where id=#{id} "})
    void updateCreatedTime(@Param("id")int id,@Param("date")Date date);
}
