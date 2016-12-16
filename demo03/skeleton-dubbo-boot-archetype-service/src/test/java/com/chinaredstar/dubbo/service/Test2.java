package com.chinaredstar.dubbo.service;

import com.chinaredstar.dubbo.api.IUserService;
import com.chinaredstar.dubbo.api.vo.UserVo;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 测试点
 * <p>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext1.xml"})

public class Test2 {
    @Autowired
    private IUserService userService;

    @Test
    public void test1() throws Exception {
        UserVo userVo = new UserVo(2013118100,"leijianxiong","930818");
        userService.add(userVo);
    }
}
