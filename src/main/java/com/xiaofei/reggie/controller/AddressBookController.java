package com.xiaofei.reggie.controller;


import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.AddressBook;
import com.xiaofei.reggie.service.AddressBookService;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.utils.ThreadContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Qualifier("addressBookServiceImpl")
    @Autowired
    private AddressBookService addressBookService;
    @PostMapping
    public R<AddressBook> save (@RequestBody AddressBook addressBook) {
        //从线程获取id
        addressBook.setUserId(ThreadContext.getSessionOfThreadId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        return addressBookService.setDefault(addressBook);
    }
    @GetMapping("/{id}")
    public R get (@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null){
            return R.success(addressBook) ;
        } else {
            return R.error("没有找到该对象");
        }
    }
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        return addressBookService.getDefault();
    }
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        return addressBookService.selectList(addressBook);
    }
    //http://localhost/addressBook?ids=1687453778578903041 DELETE
    @DeleteMapping
    public R<String> deleteByid(@PathParam("ids") Long ids){
        return addressBookService.deleteByid(ids);
    }
}
