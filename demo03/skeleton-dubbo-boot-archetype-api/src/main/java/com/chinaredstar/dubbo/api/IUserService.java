package com.chinaredstar.dubbo.api;

import com.chinaredstar.dubbo.api.vo.UserVo;

public interface IUserService {
    void add(UserVo userVo);

    UserVo findById(Integer id);

    UserVo update(UserVo userVo);

    void deleteById(Integer id);
}
