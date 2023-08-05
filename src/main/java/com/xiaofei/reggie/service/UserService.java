package com.xiaofei.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Service
public interface UserService extends IService<User> {

    R<User> userLogin(Map map, HttpServletRequest request);
    R<String> userSendMsg(User user, HttpServletRequest request);

}
