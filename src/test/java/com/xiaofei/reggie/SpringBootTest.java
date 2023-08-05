package com.xiaofei.reggie;

import com.xiaofei.reggie.utils.SMSUtils;
import org.junit.jupiter.api.Test;

@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {
    @Test
    public void sms(){
        SMSUtils.sendMessage("juju","2345","17773982935", "成功");

    }
}
