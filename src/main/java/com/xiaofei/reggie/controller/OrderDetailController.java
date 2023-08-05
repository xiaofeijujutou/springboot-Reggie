package com.xiaofei.reggie.controller;


import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {
    @Qualifier("orderDetailServiceImpl")
    @Autowired
    private OrderDetailService orderDetailService;


}
