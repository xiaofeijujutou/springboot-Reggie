package com.xiaofei.reggie.dto;


import com.xiaofei.reggie.entity.Dish;
import com.xiaofei.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;


public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

    public List<DishFlavor> getFlavors() {
        return flavors;
    }

    public void setFlavors(List<DishFlavor> flavors) {
        this.flavors = flavors;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }
}
