package com.csse.service;

import com.csse.domain.UserEntity;

public interface LoginUserService {
    /**
     * 新增用户
     * @param userEntity-用户实体
     */
    void addUser(UserEntity userEntity);

    /**
     * 用户查询
     * @param username
     * @return UserEntity-用户实体
     */
    UserEntity selectUser(String username);
}
