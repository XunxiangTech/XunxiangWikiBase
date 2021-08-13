package com.xunxiang.xunxiangwikibase.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xunxiang.xunxiangwikibase.common.enums.RoleEnum;
import com.xunxiang.xunxiangwikibase.common.exception.BusinessException;
import com.xunxiang.xunxiangwikibase.common.exception.BusinessExceptionCode;
import com.xunxiang.xunxiangwikibase.domain.*;
import com.xunxiang.xunxiangwikibase.mapper.RoleUserMapper;
import com.xunxiang.xunxiangwikibase.mapper.UserMapper;
import com.xunxiang.xunxiangwikibase.req.UserLoginReq;
import com.xunxiang.xunxiangwikibase.req.UserRegisterReq;
import com.xunxiang.xunxiangwikibase.resp.UserLoginResp;
import com.xunxiang.xunxiangwikibase.service.UserService;
import com.xunxiang.xunxiangwikibase.util.CopyUtil;
import com.xunxiang.xunxiangwikibase.util.SnowFlake;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleUserMapper roleUserMapper;

    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private PermissionServiceImpl permissionService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;

    @Override
    public List<User> listAllUser() {
        List<User> userList = userMapper.listAll();
        return userList;
    }

    @Override
    public User findByUsername(String username) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);
        if(!ObjectUtils.isEmpty(userList)){
            return userList.get(0);
        }
        throw new AuthenticationException("该用户不存在");
    }

    @Override
    public UserLoginResp login(UserLoginReq userLoginReq) {

        //User user = findByUsername(userLoginReq.getUsername())
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().getId();

        String username = userLoginReq.getUsername();
        String password = userLoginReq.getPassword();

        UsernamePasswordToken token = new UsernamePasswordToken(username,password);

        System.out.println("LOGIN TOKEN: "+token);
        subject.login(token);
        User user = findByUsername(username);
        UserLoginResp loginResp = CopyUtil.copy(user,UserLoginResp.class);
        loginResp.setToken(subject.getSession().getId().toString());

        List<String> permissions = new ArrayList<>();
        List<Role> roleList = roleService.findByUsername(username);
        for(Role r:roleList){
            List<Permission> permissionList = permissionService.findByRoleId(r.getId());
            permissions.addAll(permissionList.stream().map(Permission::getPermission).collect(Collectors.toList()));
        }
        loginResp.setPermissions(permissions);
//        redisTemplate.opsForValue().set(token.toString(),loginResp,3600, TimeUnit.SECONDS);
//        } catch (IncorrectCredentialsException e) {
//            jsonObject.put("msg", "密码错误");
//        } catch (LockedAccountException e) {
//            jsonObject.put("msg", "登录失败，该用户已被冻结");
//        } catch (AuthenticationException e) {
//            jsonObject.put("msg", "该用户不存在");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return loginResp;
        }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Override
    public void register(UserRegisterReq userRegisterReq) {
        User user = CopyUtil.copy(userRegisterReq,User.class);
        if(ObjectUtils.isEmpty(userRegisterReq.getId())){
            if(ObjectUtils.isEmpty(selectByLoginName(userRegisterReq.getUsername()))){
                //New User
                Long userId = snowFlake.nextId();

                user.setId(userId);
                user.setState(true);
                userMapper.insert(user);
                RoleUser roleUser = new RoleUser();
                roleUser.setUserId(userId);
                roleUser.setRoleId(RoleEnum.VIEWER.getRoleId());
                roleUser.setId(snowFlake.nextId());
                roleUserMapper.insert(roleUser);
            }
            else{
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        }
        else {
            //Update
            user.setUsername(null);
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    private User selectByLoginName(String userName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(userName);
        List<User> userList = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }
        return userList.get(0);
    }
}
