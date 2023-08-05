package com.xiaofei.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.DishFlavor;
import com.xiaofei.reggie.entity.Orders;
import org.springframework.stereotype.Service;


@Service
public interface OrdersService extends IService<Orders> {

    R<String> submit(Orders orders);
}
