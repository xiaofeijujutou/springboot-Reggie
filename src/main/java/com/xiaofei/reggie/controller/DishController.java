package com.xiaofei.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.dto.DishDto;
import com.xiaofei.reggie.entity.Dish;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Qualifier("dishServiceImpl")
    @Autowired
    private DishService dishService;

    @Qualifier("dishFlavorServiceImpl")
    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto){
        return dishService.add(dishDto);
    }
    //分页查询;
    @GetMapping("/page")
    public R<Page<DishDto>> selectByPage(Integer page, Integer pageSize, String name){
        return dishService.selectByPage(page, pageSize, name);
    }

    //回显数据,回显数据其实是查两次,提交更新也可以给搞成两次
    @GetMapping("/{id}")
    public R<DishDto> updataDish(@PathVariable Long id){
        return dishService.updataDish(id);
    }

    //更新数据;
    @PutMapping
    public R<String> save(@RequestBody DishDto dishDto){
        return dishService.saveDish(dishDto);
    }

    //菜品起售停售
    @PostMapping("/status/{dataStatus}")
    public R<String> updataStatus(@PathVariable int dataStatus, @RequestParam("ids") Long[] ids){
        List<Long> idsList = (List<Long>) Arrays.asList(ids);
        return dishService.updataStatus(dataStatus, idsList);
    }
    //批量删除
    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") Long[] ids){
        List<Long> idsList = new ArrayList<>(Arrays.asList(ids));
        return dishService.deleteDish(idsList);
    }

    // http://localhost/dish/list?categoryId=1397844263642378242 GET 根据种类获取菜
    @GetMapping("/list")
    public R<List<DishDto>> SetmealList(@PathParam("categoryId") Long categoryId) {
        return dishService.SetmealList(categoryId);
    }


}
