package com.csse.controller;

import com.csse.domain.UserEntity;
import com.csse.result.RespBean;
import com.csse.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SeckillController {
    @Autowired
    private SeckillService seckillService;

    /**
     * 商品秒杀
     *
     * @param userEntity
     * @param goodsId
     * @return
     */
    @PostMapping("/doSeckill")
    public RespBean doSeckill(@RequestBody UserEntity userEntity,
                              @RequestParam Long goodsId) {
        seckillService.doSeckill(userEntity, goodsId);
        return RespBean.success();
    }
}
