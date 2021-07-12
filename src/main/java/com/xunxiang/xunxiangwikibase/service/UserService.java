package com.xunxiang.xunxiangwikibase.service;

import com.xunxiang.xunxiangwikibase.domain.User;
import com.xunxiang.xunxiangwikibase.req.UserLoginReq;
import com.xunxiang.xunxiangwikibase.resp.UserLoginResp;
import org.springframework.util.ObjectUtils;

public interface UserService {

    /**
     * 通过username查找用户信息
     * @param username 用户名
     * @return 该用户
     */
    User findByUsername(String username);

    Object login(UserLoginReq userLoginReq);
}
