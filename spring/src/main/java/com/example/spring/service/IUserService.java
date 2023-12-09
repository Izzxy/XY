package com.example.spring.service;

import com.example.spring.controller.request.BaseRequest;
import com.example.spring.controller.request.UserPageRequest;
import com.example.spring.entity.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IUserService {

    List<User> list();

    PageInfo<User> page(BaseRequest baseRequest);

    void save(User user);

    User getById(Integer id);

    void update(User user);

    void deleteById(Integer id);

    void handelAccount(User user);
}
