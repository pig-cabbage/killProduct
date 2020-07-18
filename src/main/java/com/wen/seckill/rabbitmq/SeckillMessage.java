package com.wen.seckill.rabbitmq;

import com.wen.seckill.vo.Users;
import lombok.Data;

/**
 * @Description: 用户传递消息的对象
 * @Author: Gentle
 * @date 2018/9/19  16:01
 */
@Data
public class SeckillMessage {
    private Long product_id;
    private Integer userId;
    private Integer value;


    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
