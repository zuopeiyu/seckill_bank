package com.csse.controller;

import com.csse.domain.UserEntity;
import com.csse.domain.vo.UserVO;
import com.csse.result.RespBean;
import com.csse.service.LoginUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public RespBean addUser(@RequestBody UserVO user1) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user1, userEntity);
        loginUserService.addUser(userEntity);
        return RespBean.success();
    }

}
