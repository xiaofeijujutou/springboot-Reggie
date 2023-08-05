package com.xiaofei.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.utils.ThreadContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j

public class LoginCheckFilter implements Filter {
    //Spring的工具类, 用来路径匹配, 支持通配符;
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse) response;
        //1,获取URI
        String uri = req.getRequestURI();
        //2,指定拦截路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"

        };
        //3,判断请求是否需要处理
        //请求为静态资源对象, 直接放行
        boolean check = this.check(urls, uri);
        if(check){
            chain.doFilter(req, res);
            return;
        }
        //请求为非静态资源对象, 需要验证是否登陆;
        //员工已经登陆, 放行
        if(req.getSession().getAttribute("employee") != null){
            //先getseission, 然后强转Long
            ThreadContext.setSessionOfThreadId((Long)req.getSession().getAttribute("employee"));
            chain.doFilter(req, res);
            return;
        }

        //客户已经登陆, 放行
        if(req.getSession().getAttribute("user") != null){//用户id,Long
            ThreadContext.setSessionOfThreadId((Long)req.getSession().getAttribute("user"));
            chain.doFilter(req, res);
            return;
        }

        //没登陆, 拦截, 但是前端已经写好代码了, 我们只要响应回去数据就可以;
        res.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check(String[] urls, String requestRri){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestRri);
            if (match){
                return true;
            }
        }
        return false;

    }
}
