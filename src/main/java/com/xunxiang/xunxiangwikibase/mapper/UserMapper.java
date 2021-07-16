package com.xunxiang.xunxiangwikibase.mapper;

import com.xunxiang.xunxiangwikibase.domain.User;
import com.xunxiang.xunxiangwikibase.domain.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    List<User> listAll();

    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}