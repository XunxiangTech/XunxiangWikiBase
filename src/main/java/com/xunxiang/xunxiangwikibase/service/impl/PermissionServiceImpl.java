package com.xunxiang.xunxiangwikibase.service.impl;

import com.xunxiang.xunxiangwikibase.domain.Permission;
import com.xunxiang.xunxiangwikibase.domain.Role;
import com.xunxiang.xunxiangwikibase.mapper.PermissionMapper;
import com.xunxiang.xunxiangwikibase.mapper.RoleMapper;
import com.xunxiang.xunxiangwikibase.service.PermissionService;
import com.xunxiang.xunxiangwikibase.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;
    @Override
    public List<Permission> findByRoleId(Long roleId) {
        List<Permission> permissionList = permissionMapper.findPermissionByRoleId(roleId);
        return permissionList;
    }
}
