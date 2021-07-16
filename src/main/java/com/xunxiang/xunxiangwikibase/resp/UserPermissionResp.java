package com.xunxiang.xunxiangwikibase.resp;

import java.util.List;

public class UserPermissionResp {

    private String username;
    private List<String> role;
    private List<String> permissionList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public List<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<String> permissionList) {
        this.permissionList = permissionList;
    }

    @Override
    public String toString() {
        return "UserPermissionResp{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", permissionList=" + permissionList +
                '}';
    }
}
