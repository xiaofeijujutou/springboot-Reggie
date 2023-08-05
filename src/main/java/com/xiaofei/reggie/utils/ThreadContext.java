package com.xiaofei.reggie.utils;

public class ThreadContext {
    private static ThreadLocal<Long> local = new ThreadLocal<>();

    public static void setSessionOfThreadId(Long l){
        local.set(l);
    }

    public static Long getSessionOfThreadId(){
        return local.get();
    }
}
