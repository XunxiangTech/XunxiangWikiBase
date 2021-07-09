package com.xunxiang.xunxiangwikibase.service;

import com.xunxiang.xunxiangwikibase.resp.CategoryResp;

import java.util.List;

public interface CategoryService {

    /**
     * Method to retrieve all categories
     * @return all categories
     */
    List<CategoryResp> all();
}
