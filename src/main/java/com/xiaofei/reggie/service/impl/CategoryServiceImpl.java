package com.xiaofei.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.common.DishException;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.Category;
import com.xiaofei.reggie.entity.Dish;
import com.xiaofei.reggie.entity.Setmeal;
import com.xiaofei.reggie.mapper.CategoryMapper;
import com.xiaofei.reggie.service.CategoryService;

import com.xiaofei.reggie.service.DishService;
import com.xiaofei.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public R<String> add(Category category) {
        this.save(category);
        return R.success("添加成功");
    }

    @Override
    public R<Page> selectByPage(Integer page, Integer pageSize) {
        Page<Category> returnPage = new Page(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper();
        //添加一个按照时间先后的顺序;
        wrapper.orderByAsc(Category::getSort);
        this.page(returnPage, wrapper);
        return R.success(returnPage);
    }

    @Override
    public R<String> deleteById(Long id) {//id是套餐或者集合的id
        //想要删除套餐或者菜品, 需                                                                                         要先删除里面所有的菜, 否则不能删除;
        //数据库里有属性,1对应了菜品和2对应套餐, 先要查种类
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getId, id);
        Category category = this.getOne(wrapper);//获取category的对象
        if(category.getType() == 1){//根据id去查是否有对应的菜
            LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
            dishWrapper.eq(Dish::getCategoryId, id);
            if(dishService.count(dishWrapper) > 0){//如果里面还有菜,就不能删除,给他抛异常
                throw new DishException("当前菜品分类下关联了菜品");
                //return R.error("菜品里面还有菜,不能删除");
            }else {
                this.remove(wrapper);
                return R.success("菜品删除成功");
            }

        }
        if(category.getType() == 2){
            LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
            setmealWrapper.eq(Setmeal::getCategoryId, id);
            if(setmealService.count(setmealWrapper) > 0){//如果里面还有菜,就不能删除,给他抛异常
                throw new DishException("当前套餐下关联了菜品");
                //return R.error("套餐里面还有菜,不能删除");
            }else {
                this.remove(wrapper);
                return R.success("套餐删除成功");
            }
        }
       return null;
    }

    @Override
    public List<Category> list(Category category) {
        //添加条件
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = this.list(queryWrapper);
        return list;
    }
}
