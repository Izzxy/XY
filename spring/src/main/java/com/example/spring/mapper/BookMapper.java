package com.example.spring.mapper;

import com.example.spring.controller.request.BaseRequest;
import com.example.spring.entity.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper
{
    List<Book> list();

    List<Book> listByCondition(BaseRequest baseRequest);

    void save(Book obj);

    Book getById(Integer id);

    void updateById(Book user);

    void deleteById(Integer id);

    Book getByNo(String bookNo);

    void updateNumByNo(String bookNo);

}
