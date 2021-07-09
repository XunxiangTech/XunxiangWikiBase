package com.xunxiang.xunxiangwikibase.controller;

import com.xunxiang.xunxiangwikibase.resp.CategoryResp;
import com.xunxiang.xunxiangwikibase.resp.CommonResp;
import com.xunxiang.xunxiangwikibase.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
