package com.xunxiang.xunxiangwikibase.service.impl;

import com.xunxiang.xunxiangwikibase.domain.User;
import com.xunxiang.xunxiangwikibase.domain.UserExample;
import com.xunxiang.xunxiangwikibase.mapper.UserMapper;
import com.xunxiang.xunxiangwikibase.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

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
}
