package com.xiaofei.reggie.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.dto.DishDto;
import com.xiaofei.reggie.entity.Category;
import com.xiaofei.reggie.entity.Dish;
import com.xiaofei.reggie.entity.DishFlavor;
import com.xiaofei.reggie.mapper.DishMapper;
import com.xiaofei.reggie.service.CategoryService;
import com.xiaofei.reggie.service.DishFlavorService;
import com.xiaofei.reggie.service.DishService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Qualifier("dishFlavorServiceImpl")
    @Autowired
    private DishFlavorService dishFlavorService;

    @Qualifier("categoryServiceImpl")
    @Autowired
    private CategoryService categoryService;
    @Qualifier("stringRedisTemplate")
    @Autowired
    private RedisTemplate redis;
    @Override
    @Transactional
    public R<String> add(DishDto dishDto) {
        //@Transactional是数据库事务开启,因为这里同时操作了两个表
        //因为前段只要刚点这个添加就会去查找一下category表, 所以categoryId已经被前端带过来了;
        //保存基本信息到Dish表
        this.save(dishDto);
        //flavor表, 需要另一个service
        //直接存是有问题的, 因为多种口味需要关联菜品;
        //这个dish_id是根据雪花算法生成后插入到数据库的, 需要再查询一次
        //但是这是mybatisplus自动做的，只要insert了就会将id赋值给实体类得id字段，知道就行了，想彻底搞明白，就去看底层代码吧
        Long dishId = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        //flavors= item是操作事件,item=setDishId(); 后面的collect是再转换成collect;
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

        Set keys = redis.keys("dish_*");
        redis.delete(keys);
//        String key = "dish" + dishDto.getCategoryId() + "_1";
//        redis.delete(key);
        return R.success("添加菜品成功");
    }

    @Override
    public R<Page<DishDto>> selectByPage(Integer page, Integer pageSize, String name) {
        //直接查询会导致种类没有;
        //name是模糊查询
        //wrapper里面是对表进行约束的,所以是dish不是Page<dish>
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(name), Dish::getName, name);
        wrapper.orderByDesc(Dish::getUpdateTime);

        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        this.page(dishPage, wrapper);
        //这是工具包里的对象属性复制包,忽略了records,里面就没数据了;
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        List<Dish> dishRecords = dishPage.getRecords();

        //要获取id, 就要查询根据id查询数据库, 然后得到name的名字;
        //给查出来的值修改一下操作,
        List<DishDto> dishDtoList = dishRecords.stream().map((item)->{//这括号里面的参数就是从dishRecords遍历出的脚码[0][1][2][3]
            //外面嵌套一个DishDao的集合, 然后每次遍历都创建一个list的子集合, 最后的return就是list.append添加;
            DishDto dishDto = new DishDto();
            //复制
            BeanUtils.copyProperties(item, dishDto);
            //从item获取id,然后查询得到名字
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            //有可能有些菜没有关联种类,所以最好加个判断
            if(category != null){
                String categoryName = category.getName() ;
                dishDto.setCategoryName (categoryName) ;
            }else {
                dishDto.setCategoryName("还未关联菜品");
            }
            return dishDto;
        }).collect(Collectors.toList());
        //所有的值整备好了然后返回回去;
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }
    @Override
    //用户查询//Redis改造
    public R<List<DishDto>> SetmealList(Long id, Integer status) {
        // http://localhost/dish/list?categoryId=1397844263642378242 GET 根据种类获取菜

        //先创建一个键,由用户的id组成
        String key = "dish_" + id + "_" + status;
        //从Redis读取出来,然后判断是否为空;
        String value = (String)redis.opsForValue().get(key);
        //System.out.println(value);
        List<DishDto> dtoList = JSON.parseArray(value, DishDto.class);
        if(dtoList != null){
            return R.success(dtoList);
        }
        //等于空就往下走
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);//添加条件，查询状态为1(起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        //套餐中起售的菜品
        List<Dish> dishes = this.list(queryWrapper);//添加排序条件
        //添加风味;
        dtoList = new ArrayList<>();
        dtoList = dishes.stream().map((oneDish)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(oneDish, dishDto);
            Long dishId = oneDish.getId();
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, dishId);
            dishDto.setFlavors(dishFlavorService.list(wrapper));
            return dishDto;
        }).collect(Collectors.toList());
        //查询到了,就设置到Redis里面去
        String redisList = JSON.toJSONString(dtoList);
        redis.opsForValue().set(key, redisList, 60, TimeUnit.MINUTES);
        return R.success(dtoList);
    }
    @Override
    public R<DishDto> updataDish(Long id) {
        //因为查询的数据需要风味, 所以还是选用DishDto
        DishDto dishDto = new DishDto();
        //查两张表, new两个wrapper
        LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<DishFlavor> dishFlavorWrapper = new LambdaQueryWrapper<>();
        dishWrapper.eq(Dish::getId, id);
        dishFlavorWrapper.eq(DishFlavor::getDishId, id);
        //赋值
        Dish dish = this.getOne(dishWrapper);
        dishDto.setFlavors(dishFlavorService.list(dishFlavorWrapper));
        BeanUtils.copyProperties(dish, dishDto);
        return R.success(dishDto);
    }

    @Override
    @Transactional
    public R<String> saveDish(DishDto dishDto) {
        //传入过来需要的操作:保存风味,保存菜,菜品的修改(菜品为最上层的数据库,所以直接改dish就行)
        //删除风味;
        LambdaQueryWrapper<DishFlavor> dishFlavorWrapper = new LambdaQueryWrapper<>();
        dishFlavorWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(dishFlavorWrapper);
        //保存风味, 但是这里的list只是纯字符串, 没有dishId,所以要手动插入一下;
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存菜的信息
        dishFlavorService.saveBatch(flavors);
        this.updateById(dishDto);
        Set keys = redis.keys("dish_*");
        redis.delete(keys);
//        String key = "dish" + dishDto.getCategoryId() + "_1";
//        redis.delete(key);
        return R.success("修改成功");
    }

    @Override
    public R<String> updataStatus(int status, List<Long> ids) {
        int size = ids.size();
        List<Dish> dishs = new ArrayList<>();
        for (int i = 0; i < size; i++){
            Dish dish = new Dish();
            dish.setId(ids.get(i));
            dish.setStatus(status);
            dishs.add(dish);//加一个
            //循环把ids赋值给dish
//            dishs.get(i).setId(ids.get(i));//第i个list去第i个id
//            dishs.get(i).setStatus(status);
        }
        this.updateBatchById(dishs);
        return R.success("修改成功");
    }

    @Override
    public R<String> deleteDish(List<Long> ids) {
//        int size = ids.size();
//        List<Dish> dishs = new ArrayList<>();
//        for (int i = 0; i < size; i++){
//            Dish dish = new Dish();
//            dish.setId(ids.get(i));
//            dish.setIsDeleted(1);
//            dishs.add(dish);//加一个
//        }
        this.removeByIds(ids);
        return R.success("删除成功");

    }



    @Override
    public List<R<DishDto>> getOneDishDto(Long id) {//id是菜品id,也有可能是套餐id,要求返回List
        DishDto dishDto = new DishDto();
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish, dishDto);
        List<R<DishDto>> list = new ArrayList<>();
        list.add(R.success(dishDto));
        //如果是菜的id
        return list;
    }
}
