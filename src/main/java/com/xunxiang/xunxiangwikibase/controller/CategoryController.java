package com.xunxiang.xunxiangwikibase.controller;

import com.xunxiang.xunxiangwikibase.req.CategorySaveReq;
import com.xunxiang.xunxiangwikibase.resp.CategoryResp;
import com.xunxiang.xunxiangwikibase.resp.CommonResp;
import com.xunxiang.xunxiangwikibase.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/all")
    public CommonResp all(){
        CommonResp<List<CategoryResp>> commonResp = new CommonResp<>();
        List<CategoryResp> categoryRespList = categoryService.all();
        commonResp.setContent(categoryRespList);
        commonResp.setMessage("Data Retrieve Success");
        return commonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req){
        CommonResp commonResp = new CommonResp<>();
        categoryService.save(req);
        commonResp.setMessage("Category 【"+req.getName()+"】 Successfully Edited!");
        return commonResp;
    }

    @DeleteMapping("delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp commonResp = new CommonResp<>();
        String categoryName = categoryService.select(id).getName();
        categoryService.delete(id);
        commonResp.setMessage("Category 【"+categoryName+"】 Successfully Deleted!");
        return commonResp;
    }
}
