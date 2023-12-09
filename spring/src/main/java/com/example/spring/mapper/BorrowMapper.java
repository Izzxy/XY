package com.example.spring.mapper;

import com.example.spring.controller.request.BaseRequest;
import com.example.spring.entity.Book;
import com.example.spring.entity.Borrow;
import com.example.spring.entity.Repaid;
import com.example.spring.mapper.po.BorrowRepaidCountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface BorrowMapper {

    List<Borrow> list();

    List<Borrow> listByCondition(BaseRequest baseRequest);

    List<Repaid> listRepaidByCondition(BaseRequest baseRequest);

    void save(Borrow obj);

    void saveRepaid(Repaid obj);

    Borrow getById(Integer id);

    void updateById(Borrow user);

    void deleteById(Integer id);

    void deleteRepaidById(Integer id);

    void updateStatus(String status,Integer id);

    List<BorrowRepaidCountPO> getCountByTimeRange(@Param("timeRange") String timeRage, @Param("type") int type);
}
