package com.csse.service;

import com.csse.domain.GoodsEntity;


import java.util.List;

public interface GoodsService  {

    GoodsEntity getGoodsInfoById(Long goodsId);

    void saveGoods(GoodsEntity goodsEntity);

    List<GoodsEntity> selectGoodsList();
}
