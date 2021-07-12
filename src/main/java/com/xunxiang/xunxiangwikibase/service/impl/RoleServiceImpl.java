package com.xunxiang.xunxiangwikibase.service.impl;

import com.xunxiang.xunxiangwikibase.domain.Role;
import com.xunxiang.xunxiangwikibase.mapper.RoleMapper;
import com.xunxiang.xunxiangwikibase.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> findByUsername(String username) {
        List<Role> role = roleMapper.findRoleByUsername(username);
        if(!ObjectUtils.isEmpty(role)){
            return role;
        }
        throw new NullPointerException("用户不存在");
    }
}
