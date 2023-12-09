package com.example.spring.service;

import com.example.spring.controller.request.BaseRequest;
import com.example.spring.entity.Book;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IBookService {
    List<Book> list();

    PageInfo<Book> page(BaseRequest baseRequest);

    void save(Book obj);

    Book getById(Integer id);

    void update(Book obj);

    void deleteById(Integer id);
}
