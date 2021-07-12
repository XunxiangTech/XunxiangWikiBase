package com.xunxiang.xunxiangwikibase.shiro;

import com.xunxiang.xunxiangwikibase.XunxiangwikibaseApplication;
import com.xunxiang.xunxiangwikibase.domain.User;
import com.xunxiang.xunxiangwikibase.mapper.PermissionMapper;
import com.xunxiang.xunxiangwikibase.mapper.RoleMapper;
import com.xunxiang.xunxiangwikibase.service.UserService;
import com.xunxiang.xunxiangwikibase.service.impl.UserServiceImpl;
import com.xunxiang.xunxiangwikibase.util.CopyUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class ShiroRealm extends AuthorizingRealm {
    private static final Logger LOG = LoggerFactory.getLogger(ShiroRealm.class);

    @Resource
    UserServiceImpl userService;

    @Resource
    RoleMapper roleMapper;

    @Resource
    PermissionMapper permissionMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        LOG.info("权限配置-->ShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = CopyUtil.copy(principalCollection.getPrimaryPrincipal(),User.class);
        roleMapper.findRoleByUsername(user.getUsername()).stream().forEach(
                role -> {
                    authorizationInfo.addRole(role.getRole());
                    permissionMapper.findPermissionByRoleId(role.getId()).stream().forEach(
                            permission -> {
                                authorizationInfo.addStringPermission(permission.getPermission());
                            }
                    );
                }
        );

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        LOG.info("ShiroRealm.doGetAuthenticationInfo()");

        // 获取用户名密码
        String username = (String) authenticationToken.getPrincipal();
        LOG.info(authenticationToken.getCredentials().toString());

        User user = userService.findByUsername(username);
        LOG.info("User is--> "+username);

        if(user==null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                getName()
        );
        return authenticationInfo;
    }

//    /**
//     * 通过用户名清除缓存
//     */
//    public void clearCache(String username) {
//        System.out.println("调用cache清理操作");
//        PrincipalCollection principals = new SimplePrincipalCollection(
//                new UserPrivacy(username), getName());
//        clearCache(principals);
//    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
}
