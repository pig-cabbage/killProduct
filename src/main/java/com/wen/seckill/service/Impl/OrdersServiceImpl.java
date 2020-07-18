package com.wen.seckill.service.Impl;

import com.wen.seckill.constant.Constants;
import com.wen.seckill.domian.SeckillOrder;
import com.wen.seckill.exception.CheckException;
import com.wen.seckill.factory.OrdersFactory;
import com.wen.seckill.mapper.OrdersMapper;
import com.wen.seckill.mapper.ProductMapper;
import com.wen.seckill.redis.RedisService;
import com.wen.seckill.service.OrdersService;
import com.wen.seckill.utils.JsonUtils;
import com.wen.seckill.utils.Sequence;

import com.wen.seckill.vo.Orders;
import com.wen.seckill.vo.Product;
import com.wen.seckill.vo.Users;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description: 订单操作
 * @Author: Gentle
 * @date 2018/9/17  13:55
 */
@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    RedisService redisService;

    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrdersMapper ordersMapper;

    /**
     * 判断是否重复下单
     *
     * @param userId
     * @param productId
     * @return
     */
    @Override
    public SeckillOrder isRepeatSeckill(int userId, long productId) {

        String value = redisService.get(Constants.USER_PRODUCT_BY_SECKILL + ":" + userId + "_" + productId);
        if (value != null) {
            return JsonUtils.jsonToObject(value, SeckillOrder.class);
        }
        return null;
    }

    /**
     * 减少库存数量
     *
     * @param productId
     * @param version
     * @return
     */
    @Override
    public boolean reduceProductNumber(long productId, int version, int value) {
        int temp = productMapper.reduceProductNumber(productId, version, value);
        if (temp != 1) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public Orders doSeckill(Product product, Integer userId, Integer value) {


        boolean flag = reduceProductNumber(product.getProductid(), product.getVersion(), value);
        if (flag) {
            //获取单例对象生成订单。
            Sequence sequence = OrdersFactory.getInstance();
            Orders order = new Orders();
            order.setProductid(product.getProductid());
            order.setUserid(userId);
            order.setStatus(0);
            order.setOrderid("" + sequence.nextId());

            //插入数据库
            insertOrder(order);

            return order;
        } else {
            throw new CheckException("商品售完了~");
        }
    }

    /**
     * 抢购订单入库
     *
     * @param seckillOrder
     * @return
     */
    @Override
    public int insertOrder(Orders seckillOrder) {
        return ordersMapper.insertOrder(seckillOrder);
    }


}
