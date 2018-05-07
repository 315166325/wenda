package com.nowcoder.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AOPclass {
    private static final Logger logger = LoggerFactory.getLogger(AOPclass.class);
    public void A(){
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(i);
        }
        logger.info("A方法执行中");
        this.B();
    }
    public void B(){
        logger.info("B方法执行中");
    }



}
