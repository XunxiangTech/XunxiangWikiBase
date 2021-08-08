package com.xunxiang.xunxiangwikibase.service;

import com.xunxiang.xunxiangwikibase.domain.User;
import com.xunxiang.xunxiangwikibase.req.UserLoginReq;
import com.xunxiang.xunxiangwikibase.req.UserRegisterReq;
import com.xunxiang.xunxiangwikibase.resp.UserLoginResp;
import org.springframework.util.ObjectUtils;

import java.util.List;

public interface UserService {

    List<User> listAllUser();
    /**
     * 通过username查找用户信息
     * @param username 用户名
     * @return 该用户
     */
    User findByUsername(String username);

    /**
     * 登录用户
     * @param userLoginReq
     * @return
     */
    UserLoginResp login(UserLoginReq userLoginReq);

    /**
     * Register User
     * @param userRegisterReq
     */
    void register(UserRegisterReq userRegisterReq);

    /**
     * Delete User based on Id
     * @param id
     */
    void delete(Long id);

}
