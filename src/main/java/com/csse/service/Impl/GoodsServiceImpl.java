package com.csse.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csse.domain.GoodsEntity;
import com.csse.domain.vo.GoodsVO;
import com.csse.mapper.GoodsMapper;
import com.csse.service.GoodsService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public GoodsEntity getGoodsInfoById(Long goodsId) {
        GoodsEntity goodsEntity = goodsMapper.selectById(goodsId);
        return goodsEntity;
    }

    @Override
    public void saveGoods(GoodsEntity goodsEntity) {
        try {
            int insert = goodsMapper.insert(goodsEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<GoodsEntity> selectGoodsList() {
        List<GoodsEntity> goodsEntities = goodsMapper.selectList(new QueryWrapper<>());

//        List<GoodsVO> goodsVOS = goodsEntities.stream().map(goos -> {
//            GoodsVO goodsVO = new GoodsVO();
//            BeanUtils.copyProperties(goos, goodsVO);
//            return goodsVO;
//        }).collect(Collectors.toList());
        return goodsEntities;
    }

}
