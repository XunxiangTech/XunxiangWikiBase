package com.xunxiang.xunxiangwikibase.service;

import com.xunxiang.xunxiangwikibase.domain.Role;
import com.xunxiang.xunxiangwikibase.domain.User;

import java.util.List;

public interface RoleService {

    /**
     * 通过username查找用户信息
     * @param username 用户名
     * @return 该用户
     */
    List<Role> findByUsername(String username);
}
