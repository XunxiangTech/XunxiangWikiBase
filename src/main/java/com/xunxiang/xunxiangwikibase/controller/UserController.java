package com.xunxiang.xunxiangwikibase.controller;


import com.xunxiang.xunxiangwikibase.domain.Permission;
import com.xunxiang.xunxiangwikibase.domain.Role;
import com.xunxiang.xunxiangwikibase.domain.User;
import com.xunxiang.xunxiangwikibase.mapper.PermissionMapper;
import com.xunxiang.xunxiangwikibase.req.UserLoginReq;
import com.xunxiang.xunxiangwikibase.req.UserRegisterReq;
import com.xunxiang.xunxiangwikibase.resp.CommonResp;
import com.xunxiang.xunxiangwikibase.resp.UserLoginResp;
import com.xunxiang.xunxiangwikibase.service.PermissionService;
import com.xunxiang.xunxiangwikibase.service.impl.PermissionServiceImpl;
import com.xunxiang.xunxiangwikibase.service.impl.RoleServiceImpl;
import com.xunxiang.xunxiangwikibase.service.impl.UserServiceImpl;
import com.xunxiang.xunxiangwikibase.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserServiceImpl userService;

    @Resource
    private PermissionServiceImpl permissionService;

    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RedisTemplate redisTemplate;


    @Resource
    SnowFlake snowFlake;

    @GetMapping("/find-user")
    public CommonResp findUser(String username){
        CommonResp resp = new CommonResp();
        User user = userService.findByUsername(username);
        resp.setContent(user);
        return resp;
    }

    @GetMapping("/find-role")
    public CommonResp findRole(String username){
        CommonResp resp = new CommonResp();
        List<Role> role = roleService.findByUsername(username);
        resp.setContent(role);
        return resp;
    }

    @GetMapping("/find-perm")
    public CommonResp findPerm(Long roleId){
        CommonResp resp = new CommonResp();
        List<Permission> permissions = permissionService.findByRoleId(roleId);
        resp.setContent(permissions);
        return resp;
    }


    @PostMapping("/register")
    public CommonResp register(@Valid @RequestBody UserRegisterReq req){
        return null;
    }
    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req){
        //req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<Object> resp = new CommonResp<>();
        Object loginResp = userService.login(req);
        //Long token =snowFlake.nextId();
        //LOG.info("生成单点登录token：{}，并放入redis中", token);
//        loginResp.setToken(token.toString());
        //redisTemplate.opsForValue().set(token,loginResp,3600*24, TimeUnit.SECONDS);
        resp.setContent(loginResp);
        return resp;
    }
}
