package com.example.spring.service;

import com.example.spring.controller.request.BaseRequest;
import com.example.spring.entity.Borrow;
import com.example.spring.entity.Repaid;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IBorrowService {
    List<Borrow> list();

    PageInfo<Borrow> page(BaseRequest baseRequest);

    void save(Borrow obj);

    PageInfo<Repaid> pageRepaid(BaseRequest baseRequest);

    void saveRepaid(Repaid obj);

    Borrow getById(Integer id);

    void update(Borrow obj);

    void deleteById(Integer id);

    void deleteRepaidById(Integer id);

    Object getCountByTimeRange(String timeRage);
}
