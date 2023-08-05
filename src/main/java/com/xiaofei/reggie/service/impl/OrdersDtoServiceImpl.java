package com.xiaofei.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.dto.OrdersDto;
import com.xiaofei.reggie.entity.DishFlavor;
import com.xiaofei.reggie.mapper.DishFlavorMapper;
import com.xiaofei.reggie.mapper.OrdersDtoMapper;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.OrdersDtoService;
import org.springframework.stereotype.Service;


@Service
public class OrdersDtoServiceImpl extends ServiceImpl<OrdersDtoMapper, OrdersDto> implements OrdersDtoService {

}
