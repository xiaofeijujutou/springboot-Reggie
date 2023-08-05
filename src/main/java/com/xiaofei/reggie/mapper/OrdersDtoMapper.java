package com.xiaofei.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaofei.reggie.dto.OrdersDto;
import com.xiaofei.reggie.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersDtoMapper extends BaseMapper<OrdersDto> {
}
