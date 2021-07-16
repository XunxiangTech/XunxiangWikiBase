package com.xunxiang.xunxiangwikibase.service;

import com.xunxiang.xunxiangwikibase.domain.Permission;
import com.xunxiang.xunxiangwikibase.domain.Role;
import com.xunxiang.xunxiangwikibase.req.UserPermissionReq;
import com.xunxiang.xunxiangwikibase.resp.UserPermissionResp;

import java.util.List;

public interface PermissionService {

    /**
     *
     * @param roleId
     * @return
     */
    List<Permission> findByRoleId(Long roleId);

    /**
     * List all possible roles
     * @return
     */
    List<Role> listAllRole();

    /**
     * List all User-Role-Permissions for User Management Page
     * @return
     */
    List<UserPermissionResp> listAllUserPermission();

    void save(UserPermissionReq req);
}
