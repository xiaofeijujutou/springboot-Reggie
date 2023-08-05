package com.xiaofei.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.dto.SetmealDto;
import com.xiaofei.reggie.entity.Setmeal;
import com.xiaofei.reggie.service.DishService;
import com.xiaofei.reggie.service.SetmealDishService;
import com.xiaofei.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Qualifier("setmealServiceImpl")
    @Autowired
    private SetmealService service;

    @Qualifier("setmealDishServiceImpl")
    @Autowired
    private SetmealDishService setmealDishService;

//    http://localhost/setmeal  POST
    @PostMapping
    public R<String> add(@RequestBody SetmealDto setmealDto){
        return service.add(setmealDto);
    }
    //http://localhost/setmeal/page?page=1&pageSize=10  GET
    @GetMapping("/page")
    public R<Page<SetmealDto>> selectByPage(Integer page, Integer pageSize, String name){
        return service.selectByPage(page, pageSize, name);
    }
    //http://localhost/setmeal/1415580119015145474  GET//数据回显
    @GetMapping("/{id}")
    public R<SetmealDto> review(@PathVariable Long id){
        return service.review(id);
    }
    //http://localhost/setmeal?ids=1687038701912711170 DELETE
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam("ids") Long[] ids){
        List<Long> idsList = new ArrayList<>(Arrays.asList(ids));
        return service.deleteDish(idsList);
    }
    //起售停售
    //http://localhost/setmeal/status/0?ids=1415580119015145474 POST
    @PostMapping("/status/{dataStatus}")
    public R<String> updataStatus(@PathVariable int dataStatus, @RequestParam("ids") Long[] ids){
        List<Long> idsList = (List<Long>) Arrays.asList(ids);
        return service.updataStatus(dataStatus, idsList);
    }
    //修改更新请求
    //http://localhost/setmeal PUT
    @PutMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        return service.saveSetmeal(setmealDto);
    }

//    http://localhost/setmeal/list?categoryId=1687037615554420738&status=1 GET
    @GetMapping("/list")
    public R<List<SetmealDto>> userList(@PathParam("categoryId") Long categoryId, @PathParam("status")Integer status) {
        return service.userList(categoryId, status);
    }

}
