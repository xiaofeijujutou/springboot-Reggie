package com.xiaofei.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService extends IService<Category> {
    R<String> add(Category category);

    R<Page> selectByPage(Integer page, Integer pageSize);

    R<String> deleteById(Long id);

    List<Category> list(Category category);
}
