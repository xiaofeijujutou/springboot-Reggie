package com.xiaofei.reggie.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.dto.DishDto;
import com.xiaofei.reggie.entity.Category;
import com.xiaofei.reggie.entity.Dish;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DishService extends IService<Dish> {

    R<String> add(DishDto dishDto);

    R<Page<DishDto>> selectByPage(Integer page, Integer pageSize, String name);

    R<DishDto> updataDish(Long id);

    R<String> saveDish(DishDto dishDto);

    R<String> updataStatus(int status, List<Long> ids);

    R<String> deleteDish(List<Long> idsList);

    //R<List<Dish>> SetmealList(Long categoryId);

    List<R<DishDto>> getOneDishDto(Long id);

    R<List<DishDto>> SetmealList(Long categoryId);
}
