package com.xiaofei.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.Employee;
import com.xiaofei.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Qualifier("employeeServiceImpl")
    @Autowired
    private EmployeeService service;

    //employee/login
    @PostMapping("/login")
    //request是为了在后面的操作中快速获取到用户的登陆信息, 用来getSession;
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request){
        return service.login(employee, request);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.removeAttribute("employee");
        return R.success("登出成功");
    }

    @PostMapping
    public R<Employee> add(@RequestBody Employee employee, HttpServletRequest request){
        R r = service.add(employee, request);
        return r;
    }

    @GetMapping("/page")
    public R<Page> selectByPage(Integer page, Integer pageSize, String name){
        R<Page> r = service.selectByPage(page, pageSize, name);
        return r;
    }

    @PutMapping
    public R<String> updataStatus(HttpServletRequest request, @RequestBody Employee employee){
        R<String> r = service.updataStatus(request, employee);
        return r;
    }
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = service.getById(id);
        return R.success(employee);
    }



}
