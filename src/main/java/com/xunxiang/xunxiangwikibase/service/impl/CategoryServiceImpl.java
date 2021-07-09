package com.xunxiang.xunxiangwikibase.service.impl;

import com.xunxiang.xunxiangwikibase.domain.Category;
import com.xunxiang.xunxiangwikibase.domain.CategoryExample;
import com.xunxiang.xunxiangwikibase.mapper.CategoryMapper;
import com.xunxiang.xunxiangwikibase.req.CategorySaveReq;
import com.xunxiang.xunxiangwikibase.resp.CategoryResp;
import com.xunxiang.xunxiangwikibase.service.CategoryService;
import com.xunxiang.xunxiangwikibase.util.CopyUtil;
import com.xunxiang.xunxiangwikibase.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    SnowFlake snowFlake;

    @Override
    public Category select(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CategoryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        List<CategoryResp> categoryRespList = CopyUtil.copyList(categoryList,CategoryResp.class);

        return categoryRespList;
    }

    @Override
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req,Category.class);
        if(ObjectUtils.isEmpty(req.getId())){
            // Create Category
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }
        else{
            // Update Category
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
