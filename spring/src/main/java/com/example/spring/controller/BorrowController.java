package com.example.spring.controller;

import com.example.spring.common.Result;
import com.example.spring.controller.request.BorrowPageRequest;
import com.example.spring.entity.Borrow;
import com.example.spring.entity.Repaid;
import com.example.spring.service.IBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    IBorrowService borrowService;

    @PostMapping("/save")
    public Result save(@RequestBody Borrow obj) {
        borrowService.save(obj);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Borrow obj) {
        borrowService.update(obj);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        borrowService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Borrow obj = borrowService.getById(id);
        return Result.success(obj);
    }

    @GetMapping("/list")
    public Result list() {
        List<Borrow> list = borrowService.list();
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result page(BorrowPageRequest pageRequest) {

        return Result.success(borrowService.page(pageRequest));
    }

    @GetMapping("/pageRepaid")
    public Result pageRepaid(BorrowPageRequest pageRequest) {

        return Result.success(borrowService.pageRepaid(pageRequest));
    }

    @PostMapping("/saveRepaid")
    public Result saveRepaid(@RequestBody Repaid obj) {
        borrowService.saveRepaid(obj);
        return Result.success();
    }

    @DeleteMapping("/deleteRepaid/{id}")
    public Result deleteRepaid(@PathVariable Integer id) {
        borrowService.deleteRepaidById(id);
        return Result.success();
    }

    @GetMapping("/lineCharts/{timeRage}")
    public Result lineCharts(@PathVariable String timeRage) {
        return Result.success(borrowService.getCountByTimeRange(timeRage));
    }
}
