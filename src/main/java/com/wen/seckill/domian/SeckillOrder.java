package com.wen.seckill.domian;

import lombok.Data;

import java.util.Date;

/**
 * @Description: 秒杀订单
 * @Author: Gentle
 * @date 2018/9/17  22:49
 */
@Data
public class SeckillOrder {
    private Long order_id;
    private Long product_id;
    private Integer user_id;
    private Date order_time;
    private Integer status;

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Date getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Date order_time) {
        this.order_time = order_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
