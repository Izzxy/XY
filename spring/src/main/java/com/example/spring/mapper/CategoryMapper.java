package com.example.spring.mapper;

import com.example.spring.controller.request.BaseRequest;
import com.example.spring.controller.request.PasswordRequest;
import com.example.spring.entity.Category;
import com.example.spring.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper
{
    List<Category> list();

    List<Category> listByCondition(BaseRequest baseRequest);

    void save(Category obj);

    Category getById(Integer id);

    void updateById(Category user);

    void deleteById(Integer id);

}
