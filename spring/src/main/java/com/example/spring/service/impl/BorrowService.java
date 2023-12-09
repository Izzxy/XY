package com.example.spring.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.spring.controller.request.BaseRequest;
import com.example.spring.entity.Book;
import com.example.spring.entity.Borrow;
import com.example.spring.entity.Repaid;
import com.example.spring.entity.User;
import com.example.spring.exception.ServiceException;
import com.example.spring.mapper.BookMapper;
import com.example.spring.mapper.BorrowMapper;
import com.example.spring.mapper.UserMapper;
import com.example.spring.mapper.po.BorrowRepaidCountPO;
import com.example.spring.service.IBorrowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class BorrowService implements IBorrowService {

    @Resource
    BorrowMapper borrowMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    BookMapper bookMapper;

    @Override
    public List<Borrow> list() {
        return borrowMapper.list();
    }

    @Override
    public PageInfo<Borrow> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(),baseRequest.getPageSize());
        List<Borrow> borrows = borrowMapper.listByCondition(baseRequest);
        for (Borrow borrow : borrows) {
            LocalDate returnDate = borrow.getReturnDate();
            LocalDate now = LocalDate.now();
            if (now.plusDays(1).isEqual(returnDate)) { //当前日期比归还的日期小一天
                borrow.setNote("即将到期");
            } else if (now.isEqual(returnDate)) {
                borrow.setNote("已到期");
            } else if (now.isAfter(returnDate)) {
                borrow.setNote("已过期");
            } else {
                borrow.setNote("正常");
            }
        }
        return new PageInfo<>(borrows);
    }

    @Override
    @Transactional
    public void save(Borrow obj) {
        //1、校验用户积分是否足够
        String userNo = obj.getUserNo();
        User user = userMapper.getByUsername(userNo);
        if (Objects.isNull(user)) {
            throw new ServiceException("用户不存在");
        }
        //2、校验图书数量是否足够
        Book book = bookMapper.getByNo(obj.getBookNo());
        if (Objects.isNull(book)) {
            throw new ServiceException("所借图书不存在");
        }
        Integer account = user.getAccount();
        Integer score = book.getScore() * obj.getDays();  //score = 借1本的积分* 天数
        //3、校验图书数量
        if (book.getNums() < 1) {
            throw new ServiceException("图书数量不足");
        }
        //4、校验用户账号余额
        if (score > account) {
            throw new ServiceException("用户积分不足");
        }
        //5、更新余额
        user.setAccount(user.getAccount() - score);
        userMapper.updateById(user);
        //6、更新图书的数量
        book.setNums(book.getNums() - 1);
        bookMapper.updateById(book);

        obj.setReturnDate(LocalDate.now().plus(obj.getDays(), ChronoUnit.DAYS)); //当前的日期 days 得到归还的日期
        obj.setScore(score);
        //7、新增借书记录
        borrowMapper.save(obj);
    }

    @Override
    public PageInfo<Repaid> pageRepaid(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(),baseRequest.getPageSize());
        return new PageInfo<>(borrowMapper.listRepaidByCondition(baseRequest));
    }

    // 还书逻辑
    @Transactional
    @Override
    public void saveRepaid(Repaid obj) {
        //改状态
        obj.setStatus("已归还");
        borrowMapper.updateStatus("已归还", obj.getId()); // obj.getId() 是前端传来的借书id
        //obj.setId(null); //新数据
        obj.setRealDate(LocalDate.now());
       borrowMapper.saveRepaid(obj);
        //图书数量的增加
       bookMapper.updateNumByNo(obj.getBookNo());

       //归还和扣除用户积分
        Book book = bookMapper.getByNo(obj.getBookNo());
        if (book != null) {
            long until = 0;
            if (obj.getRealDate().isBefore(obj.getReturnDate())) {
                until = obj.getRealDate().until(obj.getReturnDate(),ChronoUnit.DAYS);
            }else if (obj.getRealDate().isAfter(obj.getReturnDate())) { //归还逾期的话，额外扣除积分
                until = -obj.getReturnDate().until(obj.getRealDate(), ChronoUnit.DAYS);
            }
            int score = (int) until * book.getScore(); //获取待归还积分
            User user = userMapper.getByUsername(obj.getUserNo());
            int account = user.getAccount() + score;
            user.setAccount(account);
            if (account < 0){
                if (score < 0) {
                    //锁定账户
                    user.setStatus(false);
                }
            }
            userMapper.updateById(user);
        }
    }

    @Override
    public Borrow getById(Integer id) {
        return borrowMapper.getById(id);
    }

    @Override
    public void update(Borrow obj) {
        obj.setUpdatetime(LocalDate.now());
        borrowMapper.updateById(obj);
    }

    @Override
    public void deleteById(Integer id) {
        borrowMapper.deleteById(id);
    }

    @Override
    public void deleteRepaidById(Integer id) {
        borrowMapper.deleteRepaidById(id);
    }

    @Override
    public Map<String, Object> getCountByTimeRange(String timeRage) {
        Map<String, Object> map = new HashMap<>();
        Date today = new Date();
        List<DateTime> dateRange;
        switch (timeRage) {
            case "week":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -6) ,today, DateField.DAY_OF_WEEK);
                break;
            case "month":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -29) ,today, DateField.DAY_OF_MONTH);
                break;
            case "month2":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -59) ,today, DateField.DAY_OF_MONTH);
                break;
            case "month3":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -89) ,today, DateField.DAY_OF_MONTH);
                break;
            default:
                dateRange = new ArrayList<>();
        }
        List<String> dateStrRange = datetimeToDateStr(dateRange);
        map.put("date",dateStrRange);
        List<BorrowRepaidCountPO> borrowCount = borrowMapper.getCountByTimeRange(timeRage,1);
        List<BorrowRepaidCountPO> repaidCount = borrowMapper.getCountByTimeRange(timeRage,2);
        map.put("borrow",countList(borrowCount,dateStrRange));
        map.put("repaid",countList(repaidCount,dateStrRange));
        return map;
    }

    private List<String> datetimeToDateStr(List<DateTime> dateTimesList) {
        List<String> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(dateTimesList)) {
            return list;
        }
        for (DateTime dateTime : dateTimesList) {
            String date = DateUtil.formatDate(dateTime);
            list.add(date);
        }
        return list;
    }

    private List<Integer> countList(List<BorrowRepaidCountPO> countPOList, List<String> dateRange) {
        List<Integer> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(countPOList)) {
            return list;
        }
        for (String date : dateRange) {
            Integer count = countPOList.stream().filter(countPO -> date.equals(countPO.getDate()))
                    .map(BorrowRepaidCountPO::getCount).findFirst().orElse(0);
            list.add(count);
        }
        return list;
    }
}
