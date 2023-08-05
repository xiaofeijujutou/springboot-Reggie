package com.xiaofei.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.Employee;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface EmployeeService extends IService<Employee> {
    R login(Employee employee, HttpServletRequest request);

    R add(Employee employee, HttpServletRequest request);

    R<Page> selectByPage(Integer page, Integer pageSize, String name);

    R<String> updataStatus(HttpServletRequest request, Employee employee);
}
