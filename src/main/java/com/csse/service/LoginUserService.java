package com.csse.service;

import com.csse.domain.User;
import com.csse.domain.UserVO;

public interface LoginUserService {
    void addUser(User user);
    User selectUser(String username);
}
