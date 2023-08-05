package com.xiaofei.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.Employee;
import com.xiaofei.reggie.mapper.EmployeeMapper;
import com.xiaofei.reggie.service.EmployeeService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.DigestException;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Override
    public R login(Employee employee, HttpServletRequest request) {
        //1,密码md5加密

        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2,根据username查询数据库
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        //eq是equalquest;
        wrapper.eq(Employee::getUsername, employee.getUsername());

        Employee emp = this.getOne(wrapper);//这是已经查询到的数据
        //3,判定数据是否合法;
        if (emp == null){
            return R.error("用户名未找到");
        }
        //4,比对密码是否正确
        if (!emp.getPassword().equals(password)){//这里要和加密过的密码比对
            return R.error("密码错误");
        }
        //5,确认账号是否被封
        if (emp.getStatus() == 0){
            return R.error("你的账号有违规行为,已被封禁");
        }
        //6,登陆成功, 把用户名存入session;
        request.getSession().setAttribute("employee", emp.getId());
        R r = R.success(emp);
        r.setMsg("登陆成功");
        return r;
    }

    @Override
    public R add(Employee employee, HttpServletRequest request) {
        //传入没有密码,要设置初始密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //数据库有创建时间,所以要有时间
        employee.setCreateTime(LocalDateTime.now());
        //这个员工什么时候修改了他的数据,也要记录下来
        employee.setUpdateTime(LocalDateTime.now());
        //因为我们现在是用的管理员账号创建,所以创建人就是管理员,从session获取
        Long id = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(id);
        employee.setUpdateUser(id);
        this.save(employee);
        return R.success("添加员工成功");
    }

    @Override
    public R<Page> selectByPage(Integer page, Integer pageSize, String name) {
        //1,构造分页构造器
        Page p = new Page(page, pageSize);
        //2,植入条件
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper();
        wrapper.like(Strings.isNotEmpty(name), Employee::getName, name);
        //添加一个按照时间先后的顺序;
        wrapper.orderByDesc(Employee::getUpdateTime);
        //3,查询
        this.page(p, wrapper);
        return R.success(p);
    }

    @Override
    public R<String> updataStatus(HttpServletRequest request, Employee employee) {
        //前段已经封装好了status,这里只要加进去就行了
        //还需要设置更新时间和更新人
        Long userId = (Long)request.getSession().getAttribute("employee");
        employee.setUpdateUser(userId);
        employee.setUpdateTime(LocalDateTime.now());
        //这里的id是身份证号
        this.updateById(employee);
        if (employee.getStatus() == 1){
            return R.success("启用成功");
        }else {
            return R.success("禁用成功");
        }

    }
}
