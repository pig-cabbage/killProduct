package com.wen.seckill.rabbitmq;

import com.wen.seckill.domian.SeckillOrder;
import com.wen.seckill.exception.CheckException;
import com.wen.seckill.redis.RedisService;
import com.wen.seckill.service.OrdersService;
import com.wen.seckill.service.ProductService;
import com.wen.seckill.utils.JsonUtils;
import com.wen.seckill.vo.Product;
import com.wen.seckill.vo.Users;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @Description: rabbitmq接收者，接收发送者的信息
 * @Author: Gentle
 * @date 2018/9/19  16:01
 */
@Component
@Slf4j
public class MQReceiver {
    @Autowired
    RedisService redisService;
    @Autowired
    ProductService productService;
    @Autowired
    OrdersService ordersService;

    /* 此处启动会报错，需要在http://localhost:15672增加miaosha.queue的对列*/
    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receiveMiaoshaMessage(String message) {
        Logger log = Logger.getLogger("asdas");

        log.info("rabbitmq接收消息");
        SeckillMessage seckillMessage = JsonUtils.jsonToObject(message, SeckillMessage.class);
        Integer userId = seckillMessage.getUserId();
        Product productInfo = productService.getProductInfo(seckillMessage.getProduct_id());
        /**
         * 判断商品数量是否符合
         */
        if (productInfo.getNumber() <= 0) {
            log.info("商品抢购已经完毕1");
            return;
        }
        /**
         * 在此判断是否重复秒杀
         */
        SeckillOrder repeatSeckill1 = ordersService.isRepeatSeckill(1, seckillMessage.getProduct_id());
        if (repeatSeckill1 != null) {
            log = Logger.getLogger("asdas");

            log.info("商品抢购已经完毕1，请不要重复秒杀");
            return;
        }
        //减库存 下订单
        ordersService.doSeckill(productInfo, userId, seckillMessage.getValue());
    }
}
