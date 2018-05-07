package com.nowcoder.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ActiveAndOnlineUsersListener  {

    @EventListener
    public void listener1(ApplicationEvent applicationEvent) {

        System.out.println("注解监听器1:"+applicationEvent.toString() );
    }
}
