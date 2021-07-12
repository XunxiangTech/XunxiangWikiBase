package com.xunxiang.xunxiangwikibase.service;

import com.xunxiang.xunxiangwikibase.domain.User;

public interface UserService {

    /**
     * 通过username查找用户信息
     * @param username 用户名
     * @return 该用户
     */
    User findByUsername(String username);
}
