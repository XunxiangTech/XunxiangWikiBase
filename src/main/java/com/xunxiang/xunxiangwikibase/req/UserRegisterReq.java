package com.xunxiang.xunxiangwikibase.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserRegisterReq {

    @NotEmpty(message = "【用户名】不能为空")
    private String username;

    @NotEmpty(message = "【邮箱】不能为空")
    private String email;

    @NotEmpty(message = "【密码】不能为空")
    // @Length(min = 6, max = 20, message = "【密码】6~20位")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "【密码】6~20位")
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserLoginReq{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}