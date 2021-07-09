package com.xunxiang.xunxiangwikibase.service.impl;

import com.xunxiang.xunxiangwikibase.domain.Category;
import com.xunxiang.xunxiangwikibase.domain.CategoryExample;
import com.xunxiang.xunxiangwikibase.mapper.CategoryMapper;
import com.xunxiang.xunxiangwikibase.resp.CategoryResp;
import com.xunxiang.xunxiangwikibase.service.CategoryService;
import com.xunxiang.xunxiangwikibase.util.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Override
    public List<CategoryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        List<CategoryResp> categoryRespList = CopyUtil.copyList(categoryList,CategoryResp.class);

        return categoryRespList;
    }
}
