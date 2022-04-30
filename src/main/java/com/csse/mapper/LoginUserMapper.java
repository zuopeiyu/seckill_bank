package com.csse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csse.domain.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginUserMapper extends BaseMapper<UserEntity> {
}
