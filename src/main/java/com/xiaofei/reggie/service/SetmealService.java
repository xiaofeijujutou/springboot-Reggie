package com.xiaofei.reggie.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.dto.DishDto;
import com.xiaofei.reggie.dto.SetmealDto;
import com.xiaofei.reggie.entity.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface SetmealService extends IService<Setmeal> {

    R<String> add(SetmealDto setmealDto);

    R<Page<SetmealDto>> selectByPage(Integer page, Integer pageSize, String name);

    R<SetmealDto> review(Long id);

    R<String> deleteDish(List<Long> idsList);

    R<String> updataStatus(int dataStatus, List<Long> idsList);

    R<String> saveSetmeal(SetmealDto setmealDto);

    R<List<SetmealDto>> userList(Long categoryId, Integer status);

}
