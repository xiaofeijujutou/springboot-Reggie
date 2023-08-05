package com.xiaofei.reggie.common;



import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class, Service.class})
@ResponseBody
public class ExceptionController {
    /**
     *用来处理sql中,发生用户名一致的错误(数据库中的账号是唯一的);
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandle(SQLIntegrityConstraintViolationException ex){
        if(ex.getMessage ().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split("");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R. error("未知错误");
    }
    @ExceptionHandler(DishException.class)
    public R<String> dishExceptionHandle(DishException cex){
        return R.error(cex.getMessage());
    }
    @ExceptionHandler(SetmealException.class)
    public R<String> setmealExceptionHandle(SetmealException sex){
        return R.error(sex.getMessage());
    }
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandle(CustomException sex){
        return R.error(sex.getMessage());
    }
}
