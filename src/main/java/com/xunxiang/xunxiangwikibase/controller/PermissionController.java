package com.xunxiang.xunxiangwikibase.controller;


import com.xunxiang.xunxiangwikibase.domain.Role;
import com.xunxiang.xunxiangwikibase.req.UserPermissionReq;
import com.xunxiang.xunxiangwikibase.resp.CommonResp;
import com.xunxiang.xunxiangwikibase.resp.UserPermissionResp;
import com.xunxiang.xunxiangwikibase.service.impl.PermissionServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/permission")
public class PermissionController {

    @Resource
    PermissionServiceImpl permissionService;

    @GetMapping("/list-all-role")
    public CommonResp listAllRole(){
        CommonResp resp = new CommonResp();
        List<Role> roleList = permissionService.listAllRole();
        resp.setMessage("List Roles Success");
        resp.setContent(roleList);
        return resp;
    }

    @GetMapping("list-all-user-permission")
    public CommonResp listAllUserPermission(){
        CommonResp resp = new CommonResp();
        List<UserPermissionResp> resps = permissionService.listAllUserPermission();
        resp.setMessage("List All User--Role--Permission Success");
        resp.setContent(resps);
        return resp;
    }

    @PostMapping("save")
    public CommonResp save(@RequestBody UserPermissionReq req){
        CommonResp resp = new CommonResp();
        permissionService.save(req);
        resp.setMessage("Role Reset for User "+req.getUsername()+" Success");
        return resp;
    }
}
