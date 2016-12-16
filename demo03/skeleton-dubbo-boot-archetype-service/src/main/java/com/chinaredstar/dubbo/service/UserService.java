package com.chinaredstar.dubbo.service;

import com.chinaredstar.dubbo.api.IUserService;
import com.chinaredstar.dubbo.api.vo.UserVo;
import com.chinaredstar.jdbc.dao.UserMapper;
import com.chinaredstar.jdbc.po.User;
import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService implements IUserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserMapper userMapper;

    @Override
    public void add(UserVo userVo) {
        User user = new User();
        user.setUserName(userVo.getName());
        user.setPassword(userVo.getPassword());
        user.setCreateTime(new Date());
        userMapper.insert(user);
        logger.info("add success");
    }

    @Override
    @Timed
    @Cacheable(cacheNames = "skeleton_user", key = "#id.toString()")
    public UserVo findById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        UserVo userVo = new UserVo();
        userVo.setId(id);
        userVo.setName(user.getUserName());
        logger.info("find success");
        return userVo;
    }

    @Override
    @CachePut(cacheNames = "skeleton_user", key = "#user.getId().toString()")
    public UserVo update(UserVo userVo) {
        User user = new User();
        user.setUserId(userVo.getId());
        user.setUserName(userVo.getName());
        userMapper.updateByPrimaryKey(user);
        logger.info("update success");
        return userVo;
    }

    @Override
    @CacheEvict(cacheNames = "skeleton_user", key = "#id.toString()")
    public void deleteById(Integer id) {
        userMapper.deleteByPrimaryKey(id);
        logger.info("delete success");
    }
}
