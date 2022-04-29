package com.csse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csse.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginUserMapper extends BaseMapper<User> {
}
