package com.xiaofei.reggie.controller;


import com.xiaofei.reggie.entity.DishFlavor;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dishFlavor")
public class DishFlavorController {
    @Qualifier("dishFlavorServiceImpl")
    @Autowired
    private DishFlavorService service;


}
