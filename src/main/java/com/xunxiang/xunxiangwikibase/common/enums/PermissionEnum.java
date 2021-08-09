package com.xunxiang.xunxiangwikibase.common.enums;

public enum PermissionEnum {
    USER_MANAGE(1L), //Admin
    POST_EDIT(2L), //Editor
    POST_CHECK(3L); //Admin

    private Long permissionId;

    PermissionEnum(Long id){
        this.permissionId=id;
    }

    public Long getRoleId() {
        return permissionId;
    }

    public void setRoleId(Long roleId) {
        this.permissionId = roleId;
    }
}
