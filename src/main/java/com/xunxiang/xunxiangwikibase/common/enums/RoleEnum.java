package com.xunxiang.xunxiangwikibase.common.enums;

public enum RoleEnum {
    ADMIN(1L),
    EDITOR(2L),
    VIEWER(3L);

    private Long roleId;
    RoleEnum(Long id){
        this.roleId=id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
