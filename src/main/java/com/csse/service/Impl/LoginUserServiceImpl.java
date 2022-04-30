package com.csse.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csse.domain.UserEntity;
import com.csse.exception.GlobalException;
import com.csse.mapper.LoginUserMapper;
import com.csse.result.RespBeanEnum;
import com.csse.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUserServiceImpl implements LoginUserService {
    @Autowired
    LoginUserMapper loginUserMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void addUser(UserEntity userEntity) {
        String password = userEntity.getPassword();
        String passwordEn = passwordEncoder.encode(password);
        userEntity.setPassword(passwordEn);
        if (loginUserMapper.insert(userEntity)!=1){
            throw new GlobalException(RespBeanEnum.SAVE_USER_ERROR);
        }
    }

    @Override
    public UserEntity selectUser(String username) {
        UserEntity userEntity = loginUserMapper.selectOne(new QueryWrapper<UserEntity>().lambda()
                .eq(UserEntity::getUserName, username));
        return userEntity;
    }

}
