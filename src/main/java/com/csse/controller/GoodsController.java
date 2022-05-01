package com.csse.controller;

import com.csse.domain.GoodsEntity;
import com.csse.domain.vo.GoodsVO;
import com.csse.result.RespBean;
import com.csse.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {


    @Autowired
    private GoodsService goodsService;

    @PostMapping("/saveGoods")
    public RespBean saveGoods(@RequestBody GoodsEntity goodsEntity) {
        goodsService.saveGoods(goodsEntity);
        return RespBean.success();
    }

    @GetMapping("/getGoodsList")
    public RespBean getGoodsList() {
        return RespBean.success(goodsService.selectGoodsList());
    }

    @GetMapping("/getGoodsInfoById")
    public RespBean getGoodsInfo(Long id) {
        GoodsEntity goodsEntity = goodsService.getGoodsInfoById(id);
        return RespBean.success(goodsEntity);
    }

}