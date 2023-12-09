package com.example.spring.mapper;

import com.example.spring.controller.request.BaseRequest;
import com.example.spring.controller.request.LoginRequest;
import com.example.spring.controller.request.PasswordRequest;
import com.example.spring.controller.request.UserPageRequest;
import com.example.spring.entity.Admin;
import com.example.spring.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper
{
    List<Admin> list();

    List<Admin> listByCondition(BaseRequest baseRequest);

    void save(Admin obj);

    Admin getById(Integer id);

    void updateById(Admin user);

    void deleteById(Integer id);

    Admin getByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    int updatePassword(PasswordRequest request);

    Admin getByUsername(String username);
}
