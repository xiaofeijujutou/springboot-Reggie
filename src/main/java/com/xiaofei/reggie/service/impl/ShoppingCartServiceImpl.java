package com.xiaofei.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.DishFlavor;
import com.xiaofei.reggie.entity.ShoppingCart;
import com.xiaofei.reggie.mapper.DishFlavorMapper;
import com.xiaofei.reggie.mapper.ShoppingCartMapper;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.ShoppingCartService;
import com.xiaofei.reggie.utils.ThreadContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Override
    public R<ShoppingCart> addShopping(ShoppingCart shoppingCart) {
        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = ThreadContext.getSessionOfThreadId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId() ;
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq (ShoppingCart::getUserId, currentId) ;
        if (dishId != null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId) ;
        }else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            //查询当前菜品或者套餐是否在购物车中
            //SQL :select * from shopping_cart where user_id = ? and dish_id/setmeal_id =
        }
        ShoppingCart cartServiceOne = this.getOne(queryWrapper);
        if(cartServiceOne != null){
            //如果已经存在，就在原来数量基础上加一
            Integer number = cartServiceOne.getNumber () ;cartServiceOne.setNumber(number + 1) ;
            this.updateById(cartServiceOne);
        }else {
            //如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return R. success(cartServiceOne) ;


    }

    @Override
    public R<List<ShoppingCart>> selectByList() {

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, ThreadContext.getSessionOfThreadId());
        wrapper.orderByAsc(ShoppingCart::getCreateTime);
        return R.success(this.list(wrapper));
    }

    @Override
    public R<String> clean() {
        //SQL : delete from shopping_cart where user_id = ?
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart ::getUserId,ThreadContext.getSessionOfThreadId());
        this.remove(queryWrapper);
        return R.success("清空购物车成功");
    }
}
