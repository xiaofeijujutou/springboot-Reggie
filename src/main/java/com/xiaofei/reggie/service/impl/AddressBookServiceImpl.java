package com.xiaofei.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.AddressBook;
import com.xiaofei.reggie.entity.DishFlavor;
import com.xiaofei.reggie.mapper.AddressBookMapper;
import com.xiaofei.reggie.mapper.DishFlavorMapper;
import com.xiaofei.reggie.service.AddressBookService;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.utils.ThreadContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Override
    //设置默认地址,取消之前的默认地址,设置新的默认地址
    public R<AddressBook> setDefault(AddressBook addressBook) {
        //log.info("addressBook: {}", addressBook) ;
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, ThreadContext.getSessionOfThreadId());
        wrapper.set(AddressBook ::getIsDefault, 0);
//SQL : update address_book set is_default = 0 where user_id = ?
        this.update (wrapper);
        addressBook.setIsDefault(1);
//SQL : update address_book set is_default = 1 where id = ?
        this.updateById(addressBook);
        return R.success(addressBook);

    }

    @Override
    //查询用户默认地址
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, ThreadContext.getSessionOfThreadId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);
//SQL : select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = this.getOne(queryWrapper);
        if (null == addressBook){
            return R. error("没有找到该对象");}
        else {
            return R. success(addressBook) ;
        }
    }

    @Override
    public R<List<AddressBook>> selectList(AddressBook addressBook) {
        addressBook.setUserId(ThreadContext.getSessionOfThreadId());
        //log.info("addressBook: {}", addressBook);
//条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(),AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
//8 C0l.select * from address_book where user_id = ?order by update_time desc
        return R.success(this.list(queryWrapper));

    }

    @Override
    public R<String> deleteByid(Long ids) {
        this.removeById(ids);
        return R.success("删除成功");
    }

}
