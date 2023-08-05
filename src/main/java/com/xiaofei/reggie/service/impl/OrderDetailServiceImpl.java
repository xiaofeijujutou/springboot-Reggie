package com.xiaofei.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.entity.DishFlavor;
import com.xiaofei.reggie.entity.OrderDetail;
import com.xiaofei.reggie.mapper.DishFlavorMapper;
import com.xiaofei.reggie.mapper.OrderDetailMapper;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;


@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
