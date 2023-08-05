package com.xiaofei.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.Category;
import com.xiaofei.reggie.entity.Dish;
import com.xiaofei.reggie.service.CategoryService;
import com.xiaofei.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Qualifier("categoryServiceImpl")
    @Autowired
    private CategoryService categoryService;

    @Qualifier("dishServiceImpl")
    @Autowired
    private DishService dishService;

    //新增
    @PostMapping
    public R<String> add(@RequestBody Category category){
        return categoryService.add(category);
    }

    //菜品分页查询;
    @GetMapping("/page")
    public R<Page> selectByPage(Integer page, Integer pageSize){
        return categoryService.selectByPage(page, pageSize);
    }

    //删除功能
    @DeleteMapping
    public R<String> deleteById(Long ids){
        return categoryService.deleteById(ids);
    }

    //修改功能
    @PutMapping
    public R<String> updata(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }
    //给添加菜品回显菜品种类
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //条件构造器
        return R.success(categoryService.list(category));

    }



}
