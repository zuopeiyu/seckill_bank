package com.csse.controller;

import com.csse.domain.GoodsEntity;
import com.csse.result.RespBean;
import com.csse.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {


    @Autowired
    private GoodsService goodsService;

    @PostMapping("/saveGoods")
    public RespBean saveGoods(@RequestBody GoodsEntity goodsEntity){
        goodsService.save(goodsEntity);
        return RespBean.success();
    }
}