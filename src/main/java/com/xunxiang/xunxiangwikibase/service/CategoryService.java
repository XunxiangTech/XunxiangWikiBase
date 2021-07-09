package com.xunxiang.xunxiangwikibase.service;

import com.xunxiang.xunxiangwikibase.domain.Category;
import com.xunxiang.xunxiangwikibase.req.CategorySaveReq;
import com.xunxiang.xunxiangwikibase.resp.CategoryResp;

import java.util.List;

public interface CategoryService {

    Category select(Long id);
    /**
     * Method to retrieve all categories
     * @return all categories
     */
    List<CategoryResp> all();

    /**
     * Save edits for categories
     */
    void save(CategorySaveReq req);

    /**
     * Delete categories
     * @param id
     */
    void delete(Long id);
}
