package com.csse.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.csse.domain.SeckillMessage;
import com.csse.mapper.OrderMapper;
import com.csse.utils.JsonUtil;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqReceiver {
    @Autowired
    OrderMapper orderMapper;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "SeckillExchange", type = "topic"),
                    key = {"seckill.#"})
    })
    public void receiveMessage(String message) {
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        orderMapper.insert(seckillMessage.getOrderEntity());

    }
}
