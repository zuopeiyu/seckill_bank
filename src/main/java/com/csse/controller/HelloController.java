package com.csse.controller;

import com.csse.domain.ResponseResult;
import com.csse.domain.User;
import com.csse.domain.UserVO;
import com.csse.service.LoginUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    @Autowired
    LoginUserService loginUserService;

    @GetMapping("/hello")
//    @PreAuthorize("hasAuthority('text')")
    public String hello() {
        return "hello,zpy";

    }

    @PostMapping("/addUser")
    public ResponseResult addUser(@RequestBody UserVO user1) {
        User user = new User();
        BeanUtils.copyProperties(user1, user);
        loginUserService.addUser(user);
        return new ResponseResult(200, "新增用户成功");
    }

    @GetMapping("/goods/toList")
    public String test() {
        return "hello";
    }
}
