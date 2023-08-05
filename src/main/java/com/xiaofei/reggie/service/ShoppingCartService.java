package com.xiaofei.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.DishFlavor;
import com.xiaofei.reggie.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ShoppingCartService extends IService<ShoppingCart> {

    R<ShoppingCart> addShopping(ShoppingCart shoppingCart);

    R<List<ShoppingCart>> selectByList();

    R<String> clean();
}
