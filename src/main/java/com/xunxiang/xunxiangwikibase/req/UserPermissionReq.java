package com.xunxiang.xunxiangwikibase.req;

import com.xunxiang.xunxiangwikibase.domain.Role;

import java.util.List;

public class UserPermissionReq {
    private List<String> roleList;

    private String username;

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserPermissionReq{" +
                "roleList=" + roleList +
                ", username='" + username + '\'' +
                '}';
    }
}
