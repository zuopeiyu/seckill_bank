package com.csse.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csse.domain.GoodsEntity;
import com.csse.domain.OrderEntity;
import com.csse.domain.SeckillMessage;
import com.csse.domain.UserEntity;
import com.csse.exception.GlobalException;
import com.csse.filter.RedisBloomFilter;
import com.csse.mapper.GoodsMapper;
import com.csse.mapper.OrderMapper;
import com.csse.result.RespBeanEnum;
import com.csse.service.GoodsService;
import com.csse.service.OrderService;
import com.csse.service.SeckillService;
import com.csse.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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
    @Autowired
    private RedisBloomFilter redisBloomFilter;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doSeckill(long userId, Long goodsId, Map<Long,Boolean> mark) {
        Long aLong = Long.valueOf(userId);
        String orderKey = "goods:" + goodsId + ",user:" + userId;
//        boolean orderKey1 = redisBloomFilter.put("orderKey", "1");
//        boolean order = redisBloomFilter.mightContain("orderKey", "1");
//        System.out.println(order);
        ValueOperations redis = redisTemplate.opsForValue();


        //1.判断库存数量
        GoodsEntity goodsEntity = goodsService.getGoodsInfoById(goodsId);
        if (goodsEntity.getGoodsStock() < 1) {
            throw new GlobalException(RespBeanEnum.EMPTY_STOCK);
        }


        //2.判断用户是否重复抢购(从redis中获取 秒杀订单信息)
//        OrderEntity orderInfo = (OrderEntity) redis.get(orderKey);
        boolean orderInfo = redisBloomFilter.mightContain("userId:" + aLong.toString(), "goodsId:" + goodsId.toString());
        if (orderInfo) {
            throw new GlobalException(RespBeanEnum.REPEATE_ERROR);
        }


        //3.秒杀商品减库存 ->redis预减库存
//        goodsEntity.setGoodsStock(goodsEntity.getGoodsStock() - 1);
//        try {
//            int update = goodsMapper.update(goodsEntity
//                    , new UpdateWrapper<GoodsEntity>().eq("id", goodsId).gt("goods_stock", 0));
//            if (update == 0) {
//                throw new GlobalException(RespBeanEnum.EMPTY_STOCK);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }2
        if (!mark.get(goodsId)){
            throw new GlobalException(RespBeanEnum.EMPTY_STOCK);
        }
        Long decrement = redis.decrement("seckill_goods::" + goodsId);
        if (decrement < 0) {
            redis.increment("seckill_goods::" + goodsId);
            goodsEntity.setGoodsStock(0);
            goodsMapper.update(goodsEntity
                    , new UpdateWrapper<GoodsEntity>().eq("id", goodsId).gt("goods_stock", 0));
            mark.put(goodsId,false);
            throw new GlobalException(RespBeanEnum.EMPTY_STOCK);
        }

        //4.订单新增
        OrderEntity orderEntity = OrderEntity.builder()
                .goodsId(goodsId).goodsCount(1)
                .goodsName(goodsEntity.getGoodsName())
                .goodsPrice(goodsEntity.getGoodsPrice())
                .status(0).userId(userId).build();

        //构造mq消息
        SeckillMessage seckillMessage = new SeckillMessage(orderEntity);
        int count=0;
        rabbitTemplate.convertAndSend("SeckillExchange", "seckill.user:"+userId, JsonUtil.object2JsonStr(seckillMessage));

//        orderMapper.insert(orderEntity);
        log.info("user:{},goodsId:{},秒杀成功", userId, goodsId);
        //5.redis中设置订单中商品id和用户id绑定
//        redis.set(orderKey, orderEntity);
        redisBloomFilter.put("userId:" + aLong.toString(), "goodsId:" + goodsId.toString());
    }
}
