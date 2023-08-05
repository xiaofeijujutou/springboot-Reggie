package com.xiaofei.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.AddressBook;
import com.xiaofei.reggie.entity.DishFlavor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface AddressBookService extends IService<AddressBook> {

    R<AddressBook> setDefault(AddressBook addressBook);

    R<AddressBook> getDefault();

    R<List<AddressBook>> selectList(AddressBook addressBook);

    R<String> deleteByid(Long ids);
}
