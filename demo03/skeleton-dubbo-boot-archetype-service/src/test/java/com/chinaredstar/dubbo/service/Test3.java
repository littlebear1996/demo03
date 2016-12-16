package com.chinaredstar.dubbo.service;

import com.chinaredstar.dubbo.api.vo.UserVo;
import com.chinaredstar.jdbc.dao.UserMapper;
import com.chinaredstar.jdbc.po.User;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.AopTestUtils;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 测试点:
 * <p>
 * 1.mybatis mapper mock
 * <p>
 * 2.cache test
 * <p>
 * 3.dubbo service test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext2.xml")
public class Test3 {
    @Mock
    UserMapper userMapper;
    @Resource
    @InjectMocks
    UserService userService;

    @Autowired
    CacheManager cacheManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        User user = new User();
        user.setUserId(1);
        user.setUserName("yangguo");
        when(userMapper.selectByPrimaryKey(1)).thenReturn(user);
        ReflectionTestUtils.setField(AopTestUtils.getTargetObject(userService), "userMapper", userMapper);
    }

    @Test
    public void test1() throws Exception {
        UserVo userVo = userService.findById(1);
        Assert.assertEquals("yangguo", userVo.getName());

        Cache cache = cacheManager.getCache("skeleton_user");
        Assert.assertEquals(((UserVo) cache.get("1").get()).getName(), "yangguo");
        verify(userMapper,times(1)).selectByPrimaryKey(1);
    }

    @After
    public void shutdown() {
        Cache cache = cacheManager.getCache("skeleton_user");
        cache.clear();
    }
}
