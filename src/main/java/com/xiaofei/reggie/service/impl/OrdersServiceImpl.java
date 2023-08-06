package com.xiaofei.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.common.CustomException;
import com.xiaofei.reggie.common.DishException;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.*;
import com.xiaofei.reggie.mapper.DishFlavorMapper;
import com.xiaofei.reggie.mapper.OrdersMapper;
import com.xiaofei.reggie.service.*;
import com.xiaofei.reggie.utils.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
@Transactional
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Qualifier("shoppingCartServiceImpl")
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Qualifier("addressBookServiceImpl")
    @Autowired
    private AddressBookService addressBookService;
    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;
    @Qualifier("orderDetailServiceImpl")
    @Autowired
    private OrderDetailService orderDetailService;
    @Override
    public R<String> submit(Orders orders) {
        Long userId = ThreadContext.getSessionOfThreadId();
        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);

        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);
        if(shoppingCarts == null || shoppingCarts.size() == 0) {
            return R.error("购物车为空，不能下单");
            //throw new CustomException("购物车为空，不能下单");
        }

        //查询用户数据
        User user = userService.getById (userId) ;
        //查询地址数据
        Long addressBookId = orders.getAddressBookId() ;
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null) {
            return R.error("用户地址信息有误，不能下单");
            //throw new CustomException("用户地址信息有误，不能下单");
        }

        long orderId = IdWorker.getId() ;//MP生成订单号

        AtomicInteger amount = new AtomicInteger(0) ;
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item. getNumber()) ;
            orderDetail.setDishFlavor(item. getDishFlavor());
            orderDetail.setDishId(item. getDishId());
            orderDetail.setSetmealId(item. getSetmealId());
            orderDetail.setName(item. getName());
            orderDetail.setImage(item. getImage());
            orderDetail.setAmount(item. getAmount()) ;
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        } ).collect(Collectors.toList()) ;



        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        orders.setId (orderId);

        //向订单表插入数据，一条数据



        //---------------------------

        //向订单表插入数据，一条数据
        this.save(orders);

        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(wrapper);
        return R.success("成功");
    }
}


