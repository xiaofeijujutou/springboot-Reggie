package com.xiaofei.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.User;
import com.xiaofei.reggie.mapper.UserMapper;
import com.xiaofei.reggie.service.UserService;
import com.xiaofei.reggie.utils.SMSUtils;
import com.xiaofei.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Service
@Slf4j
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public R<String> userSendMsg(User user, HttpServletRequest request) {
        //这个是工具包随机的4位数短信验证码,然后验证码用阿里云发给用户, 然后做验证
        String code = ValidateCodeUtils.generateValidateCode(4).toString();

        //阿里云api获取:
        //String phoneTempt = "17670841501";
        //SMSUtils.sendMessage("居居头的验证码","SMS_284120044",phoneTempt, code);
        //这个随机生成的code就相当于手机验证码,然后存入session,到时候请求登陆再比对
        request.getSession().setAttribute(user.getPhone(), code);
        System.out.println("code--------------------------->" + code);
        //log.info(code);
        return R.success("登陆成功");
    }
    @Override
    public R<User> userLogin(Map map, HttpServletRequest request) {

        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        String codeOfSession = (String)request.getSession().getAttribute(phone);

        if (Strings.isNotEmpty(codeOfSession) && code.equals(codeOfSession)){
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user = this.getOne(wrapper);
            if (user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                this.save(user);
            }
            request.getSession().setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登陆失败");
    }
}
