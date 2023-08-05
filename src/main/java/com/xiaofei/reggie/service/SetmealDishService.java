package com.xiaofei.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.dto.SetmealDto;
import com.xiaofei.reggie.entity.DishFlavor;
import com.xiaofei.reggie.entity.SetmealDish;
import org.springframework.stereotype.Service;


@Service
public interface SetmealDishService extends IService<SetmealDish> {

    R<String> add(SetmealDto setmealDto);

}
