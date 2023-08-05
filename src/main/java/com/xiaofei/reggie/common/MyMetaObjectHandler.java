package com.xiaofei.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xiaofei.reggie.utils.ThreadContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler{


    @Override
    public void insertFill (MetaObject metaObject){
//        log.info("插入操作");
//        log.info("线程id" + Thread.currentThread().getId());
//        log.info("线程设置的id" + ThreadContext.getSessionOfThreadId());
//            log. info(”公共字段自动填充[insert] ....").:
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", new Long(ThreadContext.getSessionOfThreadId())) ;
        metaObject.setValue("updateUser", new Long(ThreadContext.getSessionOfThreadId())) ;

    }
    @Override
    public void updateFill (MetaObject metaObject) {
        //log.info("更新操作");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", new Long(ThreadContext.getSessionOfThreadId()));
    }
}