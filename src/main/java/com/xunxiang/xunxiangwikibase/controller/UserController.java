package com.xunxiang.xunxiangwikibase.controller;


import com.xunxiang.xunxiangwikibase.domain.Permission;
import com.xunxiang.xunxiangwikibase.domain.Role;
import com.xunxiang.xunxiangwikibase.domain.User;
import com.xunxiang.xunxiangwikibase.mapper.PermissionMapper;
import com.xunxiang.xunxiangwikibase.resp.CommonResp;
import com.xunxiang.xunxiangwikibase.service.PermissionService;
import com.xunxiang.xunxiangwikibase.service.impl.PermissionServiceImpl;
import com.xunxiang.xunxiangwikibase.service.impl.RoleServiceImpl;
import com.xunxiang.xunxiangwikibase.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserServiceImpl userService;

    @Resource
    private PermissionServiceImpl permissionService;

    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private PermissionMapper permissionMapper;

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
}
