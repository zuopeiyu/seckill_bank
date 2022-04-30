package com.csse.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csse.domain.GoodsEntity;
import com.csse.domain.OrderEntity;
import com.csse.domain.UserEntity;
import com.csse.exception.GlobalException;
import com.csse.mapper.GoodsMapper;
import com.csse.mapper.OrderMapper;
import com.csse.result.RespBeanEnum;
import com.csse.service.GoodsService;
import com.csse.service.OrderService;
import com.csse.service.SeckillService;
import com.csse.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j

public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doSeckill(UserEntity userEntity, Long goodsId) {
        String orderKey = "goods:" + goodsId + ",user:" + userEntity.getId();
        ValueOperations redis = redisTemplate.opsForValue();
        //1.判断库存数量
        GoodsEntity goodsEntity = goodsService.getGoodsInfoById(goodsId);
        if (goodsEntity.getGoodsStock() < 1) {
            throw new GlobalException(RespBeanEnum.EMPTY_STOCK);
        }
        //2.判断用户是否重复抢购(从redis中获取 秒杀订单信息)
        OrderEntity orderInfo = (OrderEntity) redis.get(orderKey);
        if (orderInfo != null) {
            throw new GlobalException(RespBeanEnum.REPEATE_ERROR);
        }
        //3.秒杀商品减库存
        goodsEntity.setGoodsStock(goodsEntity.getGoodsStock() - 1);
        int update = goodsMapper.update(goodsEntity
                , new UpdateWrapper<GoodsEntity>().eq("id", goodsId).gt("goodsStock", 0));
        if (update == 0) {
            throw new GlobalException(RespBeanEnum.EMPTY_STOCK);
        }
        //4.订单新增
        OrderEntity orderEntity = OrderEntity.builder()
                .goodsId(goodsId).goodsCount(1)
                .goodsName(goodsEntity.getGoodsName())
                .goodsPrice(goodsEntity.getGoodsPrice())
                .status(0).build();
        orderMapper.insert(orderEntity);
        //5.redis中设置订单中商品id和用户id绑定
        redis.set(orderKey, orderEntity);
    }
}
