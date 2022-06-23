package com.csse.controller;

import com.csse.domain.GoodsEntity;
import com.csse.domain.UserEntity;
import com.csse.result.RespBean;
import com.csse.service.GoodsService;
import com.csse.service.SeckillService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class SeckillController implements InitializingBean {
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    private Map<Long, Boolean> mark = new HashMap<>();

    /**
     * 商品秒杀
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @PostMapping("/doSeckill")
    public RespBean doSeckill(@RequestParam long userId,
                              @RequestParam Long goodsId) {
        seckillService.doSeckill(userId, goodsId, mark);
        return RespBean.success();
    }

    /**
     * 系统初始化将 秒杀商品数据加载到redis
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsEntity> list = goodsService.selectGoodsList();
        list.forEach(goodsEntity -> {
            mark.put(goodsEntity.getId(), true);
            redisTemplate.opsForValue().set("seckill_goods::" + goodsEntity.getId(), goodsEntity.getGoodsStock());
        });

    }
}
