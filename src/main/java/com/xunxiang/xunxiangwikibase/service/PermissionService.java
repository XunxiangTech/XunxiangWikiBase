package com.xunxiang.xunxiangwikibase.service;

import com.xunxiang.xunxiangwikibase.domain.Permission;
import com.xunxiang.xunxiangwikibase.domain.Role;

import java.util.List;

public interface PermissionService {

    /**
     *
     * @param roleId
     * @return
     */
    List<Permission> findByRoleId(Long roleId);
}
