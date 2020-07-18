package com.wen.seckill.service;


/**
 * @Description: 秒杀服务接口
 * @Author: Gentle
 * @date 2018/9/17  9:38
 */
public interface SeckillService {

    String doSeckill(long productId, int userId, int value);

    /**
     * 初始化商品信息。用作秒杀
     *
     * @return
     */
    String reSetProduct();

}
