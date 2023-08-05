package com.xiaofei.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.dto.SetmealDto;
import com.xiaofei.reggie.entity.Dish;
import com.xiaofei.reggie.entity.DishFlavor;
import com.xiaofei.reggie.entity.SetmealDish;
import com.xiaofei.reggie.mapper.DishFlavorMapper;
import com.xiaofei.reggie.mapper.SetmealDishMapper;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.DishService;
import com.xiaofei.reggie.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
    @Qualifier("dishServiceImpl")
    @Autowired
    private DishService dishService;
    @Override
    public R<String> add(SetmealDto setmealDto) {
        return null;
    }
}
