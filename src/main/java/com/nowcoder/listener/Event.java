package com.nowcoder.listener;

import org.springframework.context.ApplicationEvent;

public class Event extends ApplicationEvent {//时间源是某个redis的键
    public Event(String pushKey) {
        super(pushKey);
    }
}
