package com.nowcoder;

import com.nowcoder.aspect.AOPclass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAOP {
    AOPclass aoPclass=new AOPclass();

    @Test
    public void testA(){
        aoPclass.A();
    }
}
