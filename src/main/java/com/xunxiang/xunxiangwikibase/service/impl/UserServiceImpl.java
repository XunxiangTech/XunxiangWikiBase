package com.xunxiang.xunxiangwikibase.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xunxiang.xunxiangwikibase.common.exception.BusinessException;
import com.xunxiang.xunxiangwikibase.common.exception.BusinessExceptionCode;
import com.xunxiang.xunxiangwikibase.domain.User;
import com.xunxiang.xunxiangwikibase.domain.UserExample;
import com.xunxiang.xunxiangwikibase.mapper.UserMapper;
import com.xunxiang.xunxiangwikibase.req.UserLoginReq;
import com.xunxiang.xunxiangwikibase.resp.UserLoginResp;
import com.xunxiang.xunxiangwikibase.service.UserService;
import com.xunxiang.xunxiangwikibase.util.CopyUtil;
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
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public User findByUsername(String username) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);
        if(!ObjectUtils.isEmpty(userList)){
            return userList.get(0);
        }
        throw new NullPointerException("用户不存在");
    }

    @Override
    public Object login(UserLoginReq userLoginReq) {

        //User user = findByUsername(userLoginReq.getUsername());
        JSONObject jsonObject = new JSONObject();
        UserLoginResp loginResp = new UserLoginResp();
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().getId();

        String username = userLoginReq.getUsername();
        String password = userLoginReq.getPassword();

        UsernamePasswordToken token = new UsernamePasswordToken(username,password);

//        try {
        subject.login(token);
            //jsonObject.put("token", subject.getSession().getId());
        jsonObject.put("msg", "登录成功");
        User user = findByUsername(username);
        loginResp = CopyUtil.copy(user,UserLoginResp.class);
        loginResp.setToken(subject.getSession().getId().toString());
        jsonObject.put("UserInfo",loginResp);
            //redisTemplate.opsForValue().set(token.toString(),loginResp,3600, TimeUnit.SECONDS);
//        } catch (IncorrectCredentialsException e) {
//            jsonObject.put("msg", "密码错误");
//        } catch (LockedAccountException e) {
//            jsonObject.put("msg", "登录失败，该用户已被冻结");
//        } catch (AuthenticationException e) {
//            jsonObject.put("msg", "该用户不存在");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return jsonObject;
        }
}
