package com.xiaofei.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.dto.SetmealDto;
import com.xiaofei.reggie.entity.Category;
import com.xiaofei.reggie.entity.Dish;
import com.xiaofei.reggie.entity.Setmeal;
import com.xiaofei.reggie.entity.SetmealDish;
import com.xiaofei.reggie.mapper.SetmealMapper;
import com.xiaofei.reggie.service.CategoryService;
import com.xiaofei.reggie.service.DishService;
import com.xiaofei.reggie.service.SetmealDishService;
import com.xiaofei.reggie.service.SetmealService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Qualifier("dishServiceImpl")
    @Autowired
    private DishService dishService;
    @Qualifier("setmealDishServiceImpl")
    @Autowired
    private SetmealDishService setmealDishService;
    @Qualifier("categoryServiceImpl")
    @Autowired
    private CategoryService categoryService;
    @Override
    public R<String> add(SetmealDto setmealDto) {
        //之前有一个空套餐, 现在要给空菜单塞菜;
        //现在保存了就会有一个setmealId,而且里面的categoryId也设置好了
        this.save(setmealDto);
        ////copies 1
        ////dishId "1397852391150759938"
        ////name "辣子鸡丁"
        ////price 8800
        //        //套餐id
        //        private Long setmealId;
        //
        //        //菜品id
        //        private Long dishId;
        //        //菜品名称 （冗余字段）
        //        private String name;
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();//最后要保存的数据从dto获取数据
        setmealDishList = setmealDishList.stream().map((setmealDish)->{
            //设置套餐中每个菜的菜品id, 设置每个菜归属套餐的套餐id
            //setmealDish.setDishId(setmealDto.getCategoryId());
            setmealDish.setSetmealId(setmealDto.getId());
            //用每个菜的id去查询对应的名字然后设置进去;
            LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
            dishWrapper.eq(Dish::getId, setmealDish.getDishId());
            Dish dish = dishService.getOne(dishWrapper);
            setmealDish.setName(dish.getName());
            return setmealDish;
        }).collect(Collectors.toList());
        //设置完毕, 保存
        setmealDishService.saveBatch(setmealDishList);
        return R.success("保存成功");
    }
    @Override
    public R<String> saveSetmeal(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        //删除原来dish操作
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(wrapper);
        //最终SetmealDish的List数据,从传过来的数据里获取一下
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        setmealDishList = setmealDishList.stream().map((setmealDish)->{
            //设置套餐中每个菜的套餐id,
            setmealDish.setSetmealId(setmealDto.getId());
            //用每个菜的id去查询对应的名字然后设置进去;
            LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
            dishWrapper.eq(Dish::getId, setmealDish.getDishId());
            Dish dish = dishService.getOne(dishWrapper);
            setmealDish.setName(dish.getName());
            return setmealDish;
        }).collect(Collectors.toList());
        //设置完毕, 保存
        setmealDishService.saveBatch(setmealDishList);
        return R.success("保存成功");

    }



    @Override
    public R<Page<SetmealDto>> selectByPage(Integer page, Integer pageSize, String name) {
        //查询获得一个Page<Setmeal>
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper();
        Page<Setmeal> pageData = new Page<>(page, pageSize);
        wrapper.like(Strings.isNotEmpty(name), Setmeal::getName, name);
        Page<Setmeal> page1 = this.page(pageData);
        Page<SetmealDto> returnPage = new Page<>();
        //复制到Page<SetmealDto>
        //protected List<T> records = Collections.emptyList();
        BeanUtils.copyProperties(page1, returnPage, "records");
        //添加Name
        List<Setmeal> setmealRecords = page1.getRecords();
        List<SetmealDto> setmealDtoRecords= new ArrayList<>();
        setmealDtoRecords = setmealRecords.stream().map((setmealItri)->{
            //先把创建一个准备返回的值, 把原来那些数据拿过来;
            SetmealDto pageSetmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmealItri, pageSetmealDto);
            //查询,判断是否为空;
            Long categoryId = setmealItri.getCategoryId();
            Category categoryEntity = categoryService.getById(categoryId);
            //有可能有些菜没有关联种类,所以最好加个判断
            if(categoryEntity != null){
                String categoryName = categoryEntity.getName() ;
                pageSetmealDto.setCategoryName(categoryName) ;
            }else {
                pageSetmealDto.setCategoryName("还未关联菜品");
            }
            return pageSetmealDto;
        }).collect(Collectors.toList());

        returnPage.setRecords(setmealDtoRecords);
        return R.success(returnPage);
//        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
//        List<Dish> dishRecords = dishPage.getRecords();
//
//        //要获取id, 就要查询根据id查询数据库, 然后得到name的名字;
//        //给查出来的值修改一下操作,
//        List<DishDto> dishDtoList = dishRecords.stream().map((item)->{//这括号里面的参数就是从dishRecords遍历出的脚码[0][1][2][3]
//            //外面嵌套一个DishDao的集合, 然后每次遍历都创建一个list的子集合, 最后的return就是list.append添加;
//            DishDto dishDto = new DishDto();
//            //复制
//            BeanUtils.copyProperties(item, dishDto);
//            //从item获取id,然后查询得到名字
//            Long categoryId = item.getCategoryId();
//            Category category = categoryService.getById(categoryId);
//            //有可能有些菜没有关联种类,所以最好加个判断
//            if(category != null){
//                String categoryName = category.getName() ;
//                dishDto.setCategoryName (categoryName) ;
//            }else {
//                dishDto.setCategoryName("还未关联菜品");
//            }
//            return dishDto;
//        }).collect(Collectors.toList());

//        setmealRecords = setmealRecords.stream().map((etmealdto)->{
//            etmealdto.setCategoryName((this.getById(etmealdto.getCategoryId())).getName());
//            return etmealdto;
//        }).collect(Collectors.toList());//返回结果是一个list

    }

    @Override
    public R<SetmealDto> review(Long id) {
        //多表查询, 这里的id是Setmeal的雪花id
        //根据id查出setmeal, 然后赋值给setmealDto
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = this.getById(id);
        BeanUtils.copyProperties(setmeal, setmealDto);
        //菜品List(用setmealid),categoryname
        //设置菜品list
        List<Long> setmealList = new ArrayList<>();
        setmealList.add(id);
        List<SetmealDish> setmealDishList = setmealDishService.listByIds(setmealList);
        setmealDto.setSetmealDishes(setmealDishList);
        //设置categoryName
        Long categoryId = setmeal.getCategoryId();
        Category category = categoryService.getById(categoryId);
        setmealDto.setCategoryName(category.getName());
        return R.success(setmealDto);
    }

    @Override
    public R<String> deleteDish(List<Long> idsList) {
        this.removeByIds(idsList);

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SetmealDish::getSetmealId, idsList);
        setmealDishService.remove(wrapper);
        return R.success("删除成功");
    }

    @Override
    public R<String> updataStatus(int dataStatus, List<Long> idsList) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId, idsList);
        List<Setmeal> setmeals = this.list(wrapper);
        setmeals = setmeals.stream().map((setmeal)->{
            setmeal.setStatus(dataStatus);
            return setmeal;
        }).collect(Collectors.toList());
        this.updateBatchById(setmeals);
        return R.success("修改成功");
    }
    @Override
    public R<List<SetmealDto>> userList(Long categoryId, Integer status) {//套餐id
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId, categoryId);
        wrapper.eq(Setmeal::getStatus, status);
        List<Setmeal> setmealList = this.list(wrapper);
        List<SetmealDto> dtoList = new ArrayList<>();
        dtoList = setmealList.stream().map((oneSetmeal)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(oneSetmeal, setmealDto);

            Long oneSetmealId = oneSetmeal.getId();
            LambdaQueryWrapper<SetmealDish> dishWrapper = new LambdaQueryWrapper<>();
            dishWrapper.eq(SetmealDish::getSetmealId, oneSetmealId);
            setmealDto.setSetmealDishes(setmealDishService.list(dishWrapper));
            return setmealDto;
        }).collect(Collectors.toList());

        return R.success(dtoList);
//        // http://localhost/dish/list?categoryId=1397844263642378242 GET 根据种类获取菜
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Dish::getCategoryId, id);//添加条件，查询状态为1(起售状态）的菜品
//        queryWrapper.eq(Dish::getStatus,1);
//        //套餐中起售的菜品
//        List<Dish> dishes = this.list(queryWrapper);//添加排序条件
//        //添加风味;
//        List<DishDto> dtoList = new ArrayList<>();
//        dtoList = dishes.stream().map((oneDish)->{
//            DishDto dishDto = new DishDto();
//            BeanUtils.copyProperties(oneDish, dishDto);
//            Long dishId = oneDish.getId();
//            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(DishFlavor::getDishId, dishId);
//            dishDto.setFlavors(dishFlavorService.list(wrapper));
//            return dishDto;
//        }).collect(Collectors.toList());
//
//        return R.success(dtoList);
    }

}
