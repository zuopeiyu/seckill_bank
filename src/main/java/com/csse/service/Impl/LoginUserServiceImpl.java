package com.csse.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csse.domain.ResponseResult;
import com.csse.domain.User;
import com.csse.mapper.LoginUserMapper;
import com.csse.service.LoginUserService;
import com.csse.utils.JwtUtil;
import com.csse.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginUserServiceImpl implements LoginUserService {
    @Autowired
    LoginUserMapper loginUserMapper;
     @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public void addUser(User user) {
        String password = user.getPassword();
        String passwordEn = passwordEncoder.encode(password);
        user.setPassword(passwordEn);
        loginUserMapper.insert(user);
    }

    @Override
    public User selectUser(String username) {
        com.csse.domain.User user = loginUserMapper.selectOne(new QueryWrapper<User>().lambda()
                .eq(com.csse.domain.User::getUserName, username));
        return user;
    }

}
