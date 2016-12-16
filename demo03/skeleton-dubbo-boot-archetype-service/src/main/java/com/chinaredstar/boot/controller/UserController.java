package com.chinaredstar.boot.controller;

import com.chinaredstar.perseus.utils.ValidationResult;
import com.chinaredstar.perseus.utils.ValidationUtils;
import com.chinaredstar.boot.vo.Result;
import com.chinaredstar.boot.vo.ResultCode;
import com.chinaredstar.dubbo.api.IUserService;
import com.chinaredstar.dubbo.api.vo.UserVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller("userController")
@RequestMapping(value = "user")
@Api(value = "user", description = "用户相关的接口")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public IUserService userService;

    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    @ApiOperation(value = "register", notes = "注册用户")
    @ResponseBody
    @RequestMapping(value = "register", method = {RequestMethod.POST})
    public Result register(@RequestBody UserVo userVoVo) {
        Result result = new Result();
        ValidationResult validationResult = ValidationUtils.validateEntity(userVoVo);
        if (validationResult.isHasErrors()) {
            result.setCode(ResultCode.C400.getCode());
            result.setValue(validationResult);
        } else {
            UserVo user = new UserVo();
            user.setName(userVoVo.getName());
            userService.add(user);

            result.setCode(ResultCode.C200.getCode());
            logger.info("register success");
        }
        return result;
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    @ApiOperation(value = "remove", notes = "删除用户")
    @ResponseBody
    @RequestMapping(value = "remove", method = {RequestMethod.POST})
    public Result remove(@RequestBody Integer id) {
        userService.deleteById(id);
        Result result = new Result();
        result.setCode(ResultCode.C200.getCode());
        logger.info("remove success");
        return result;
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    @ApiOperation(value = "modify", notes = "修改用户")
    @ResponseBody
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public Result modify(@RequestBody UserVo userVoVo) {
        UserVo user = new UserVo();
        user.setId(userVoVo.getId());
        user.setName(userVoVo.getName());
        userService.update(user);
        Result result = new Result();
        result.setCode(ResultCode.C200.getCode());
        logger.info("modify success");
        return result;
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    @ApiOperation(value = "query", notes = "查询用户")
    @ResponseBody
    @RequestMapping(value = "query/{id}", method = {RequestMethod.GET})
    public Result<UserVo> query(@PathVariable("id") Integer id, HttpServletRequest request) {
        UserVo user = userService.findById(id);
        UserVo userVoVo = new UserVo();
        userVoVo.setId(user.getId());
        userVoVo.setName(user.getName());
        Result result = new Result();
        result.setCode(ResultCode.C200.getCode());
        result.setValue(userVoVo);
        logger.info("query success");
        request.getSession().setAttribute("result", result);
        return result;
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    @ApiOperation(value = "query", notes = "查询用户")
    @ResponseBody
    @RequestMapping(value = "query", method = {RequestMethod.GET})
    public Result<UserVo> query(HttpServletRequest request) {
        logger.info("session success");
        Result<UserVo> result = (Result<UserVo>) request.getSession().getAttribute("result");
        return result;
    }
}
