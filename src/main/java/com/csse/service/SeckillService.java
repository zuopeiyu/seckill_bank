package com.csse.service;

import com.csse.domain.UserEntity;

public interface SeckillService {
    /**
     * 秒杀商品
     * @param userEntity
     * @param goodsId
     */
  void doSeckill(UserEntity userEntity, Long goodsId);
}
