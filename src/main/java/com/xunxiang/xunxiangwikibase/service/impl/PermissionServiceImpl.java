package com.xunxiang.xunxiangwikibase.service.impl;

import com.xunxiang.xunxiangwikibase.domain.*;
import com.xunxiang.xunxiangwikibase.mapper.PermissionMapper;
import com.xunxiang.xunxiangwikibase.mapper.RoleMapper;
import com.xunxiang.xunxiangwikibase.mapper.RoleUserMapper;
import com.xunxiang.xunxiangwikibase.mapper.UserMapper;
import com.xunxiang.xunxiangwikibase.req.UserPermissionReq;
import com.xunxiang.xunxiangwikibase.resp.UserPermissionResp;
import com.xunxiang.xunxiangwikibase.service.PermissionService;
import com.xunxiang.xunxiangwikibase.service.RoleService;
import com.xunxiang.xunxiangwikibase.util.SnowFlake;
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

    @Resource
    private RoleUserMapper roleUserMapper;

    @Resource
    SnowFlake snowFlake;

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

    @Override
    public void save(UserPermissionReq req) {
        List<String> roleList = req.getRoleList();
        String username = req.getUsername();
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        Long userId = userMapper.selectByExample(userExample).get(0).getId();

        RoleUserExample roleUserExample = new RoleUserExample();
        RoleUserExample.Criteria criteria1 = roleUserExample.createCriteria();
        criteria1.andUserIdEqualTo(userId);
        roleUserMapper.deleteByExample(roleUserExample);

        for(String role:roleList){
            RoleExample roleExample = new RoleExample();
            RoleExample.Criteria criteria2 = roleExample.createCriteria();
            criteria2.andRoleEqualTo(role);
            Long roleId = roleMapper.selectByExample(roleExample).get(0).getId();
            RoleUser roleUser = new RoleUser();
            roleUser.setId(snowFlake.nextId());
            roleUser.setRoleId(roleId);
            roleUser.setUserId(userId);
            roleUserMapper.insert(roleUser);
        }
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
