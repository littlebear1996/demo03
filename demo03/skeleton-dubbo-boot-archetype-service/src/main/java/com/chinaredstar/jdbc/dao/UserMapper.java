package com.chinaredstar.jdbc.dao;

import com.chinaredstar.dubbo.api.vo.UserVo;
import com.chinaredstar.jdbc.po.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}