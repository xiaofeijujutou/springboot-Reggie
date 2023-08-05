package com.xiaofei.reggie.controller;


import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.Orders;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Qualifier("ordersServiceImpl")
    @Autowired
    private OrdersService ordersService;


    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        return ordersService.submit(orders);

    }

}
