package com.xiaofei.reggie.controller;


import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.User;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redis;
    //http://localhost/user/login POST
    @PostMapping("/sendMsg")
    public R<String> userSendMsg(@RequestBody User user, HttpServletRequest request){
        return userService.userSendMsg(user, request);
    }
    //http://localhost/user/login POST
    @PostMapping("/login")
    public R<User> userLogin(@RequestBody Map map, HttpServletRequest request){
        return userService.userLogin(map, request);
    }
}
