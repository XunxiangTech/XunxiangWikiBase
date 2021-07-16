package com.xunxiang.xunxiangwikibase.service.impl;

import com.xunxiang.xunxiangwikibase.domain.Permission;
import com.xunxiang.xunxiangwikibase.domain.Role;
import com.xunxiang.xunxiangwikibase.domain.User;
import com.xunxiang.xunxiangwikibase.mapper.PermissionMapper;
import com.xunxiang.xunxiangwikibase.mapper.RoleMapper;
import com.xunxiang.xunxiangwikibase.mapper.UserMapper;
import com.xunxiang.xunxiangwikibase.resp.UserPermissionResp;
import com.xunxiang.xunxiangwikibase.service.PermissionService;
import com.xunxiang.xunxiangwikibase.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public List<Permission> findByRoleId(Long roleId) {
        List<Permission> permissionList = permissionMapper.findPermissionByRoleId(roleId);
        return permissionList;
    }

    @Override
    public List<Role> listAllRole() {
        List<Role> roleList = roleMapper.selectAll();
        return roleList;
    }

    @Override
    public List<UserPermissionResp> listAllUserPermission() {
        List<UserPermissionResp> permissionRespList = new ArrayList<>();
        List<User> userList = userMapper.listAll();
        for(User user:userList){
            permissionRespList.add(findPermRespByUsername(user.getId()));
        }
        return permissionRespList;
    }

    private UserPermissionResp findPermRespByUsername(Long id){
        UserPermissionResp resp = new UserPermissionResp();
        String username = userMapper.selectByPrimaryKey(id).getUsername();
        List<Role> roleList = roleMapper.findRoleByUsername(username);
        List<String> strRoleList = roleList.stream().map(Role::getRole).collect(Collectors.toList());
        List<String> permList = new ArrayList<>();
        for(Role role:roleList){
            Long roleId = role.getId();
            List<Permission> permissionList = findByRoleId(roleId);
            List<String> strPermissionList = permissionList.stream().map(Permission::getPermission).collect(Collectors.toList());
            permList.addAll(strPermissionList);
        }
        permList = new ArrayList<>(new HashSet<>(permList));
        resp.setUsername(username);
        resp.setRole(strRoleList);
        resp.setPermissionList(permList);
        return resp;
    }
}
