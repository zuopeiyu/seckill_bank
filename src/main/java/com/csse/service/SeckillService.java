package com.csse.service;

import com.csse.domain.UserEntity;

import java.util.Map;

public interface SeckillService {
    /**
     * 秒杀商品
     * @param userId
     * @param goodsId
     */
  void doSeckill(long userId, Long goodsId, Map<Long,Boolean> mark);
}
