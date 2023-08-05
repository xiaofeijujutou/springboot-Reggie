package com.xiaofei.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.entity.Dish;
import com.xiaofei.reggie.entity.DishFlavor;
import com.xiaofei.reggie.mapper.DishFlavorMapper;
import com.xiaofei.reggie.mapper.DishMapper;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.DishService;
import org.springframework.stereotype.Service;


@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

}
