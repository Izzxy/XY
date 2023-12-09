package com.example.spring.service;

import com.example.spring.controller.dto.LoginDTO;
import com.example.spring.controller.request.BaseRequest;
import com.example.spring.controller.request.LoginRequest;
import com.example.spring.controller.request.PasswordRequest;
import com.example.spring.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IAdminService {

    List<Admin> list();

    PageInfo<Admin> page(BaseRequest baseRequest);

    void save(Admin obj);

    Admin getById(Integer id);

    void update(Admin obj);

    void deleteById(Integer id);

    LoginDTO login(LoginRequest request);

    void changePass(PasswordRequest request);
}
