package com.csse.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csse.domain.UserEntity;
import com.csse.mapper.LoginUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private LoginUserMapper loginUserMapper;

    @Autowired
    public UserDetailsServiceImpl(LoginUserMapper loginUserMapper) {
        this.loginUserMapper = loginUserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库根据用户名查询该用户信息
        UserEntity userEntity = loginUserMapper.selectOne(new QueryWrapper<UserEntity>()
                .lambda().eq(UserEntity::getUserName, username));

        if (Objects.isNull(userEntity)) {
            throw new RuntimeException("该用户不存在");
        }
        //封装用户权限
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("text"));
        //返回userDetailsService对象
        return new org.springframework.security.core.userdetails.User(userEntity.getUserName(), userEntity.getPassword(), authorities);

    }


}
