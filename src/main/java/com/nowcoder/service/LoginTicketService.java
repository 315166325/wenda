package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.model.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoginTicketService {
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public LoginTicket selectLatestByUserId(int userId){
        return loginTicketDAO.selectLatestByUserId(userId);
    }

    public int addTicket(LoginTicket ticket){
        return loginTicketDAO.addTicket(ticket);
    }

    public List<Integer> getCurrentOnlineUsers(){
        Date current=new Date();
        List<Integer> userIds=loginTicketDAO.getCurrentOnlineUsers(current,1);
        return userIds;
    }

}
