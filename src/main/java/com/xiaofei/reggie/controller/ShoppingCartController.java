package com.xiaofei.reggie.controller;


import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.ShoppingCart;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Qualifier("shoppingCartServiceImpl")
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> addShopping(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.addShopping(shoppingCart);
    }

    //shoppingCart/list  get',
    @GetMapping("/list")
    public R<List<ShoppingCart>> selectByList() {
        return shoppingCartService.selectByList();
    }

    @DeleteMapping("/clean")
    public R<String> clean() {
        return shoppingCartService.clean();
    }

}
