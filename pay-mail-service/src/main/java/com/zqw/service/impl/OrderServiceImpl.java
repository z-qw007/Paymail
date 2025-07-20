package com.zqw.service.impl;

import com.zqw.common.constants.Constants;
import com.zqw.dao.IOrderDao;
import com.zqw.domain.po.PayOrder;
import com.zqw.domain.req.ShopCartReq;
import com.zqw.domain.res.PayOrderRes;
import com.zqw.domain.vo.ProductVO;
import com.zqw.service.IOrderService;
import com.zqw.service.rpc.ProductRPC;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Resource
    private IOrderDao orderDao;

    @Resource
    private ProductRPC productRPC;

    @Override
    public PayOrderRes createOrder(ShopCartReq shopCartReq) throws Exception {
        //1. 查询是否存在未支付订单
        PayOrder payOrderReq = new PayOrder();
        payOrderReq.setUserId(shopCartReq.getUserId());
        payOrderReq.setProductId(shopCartReq.getProductId());

        PayOrder unPaidOrder = orderDao.queryUnPayOrder(payOrderReq);
        if (unPaidOrder != null && Constants.OrderStatusEnum.PAY_WAIT.getCode().equals(unPaidOrder.getStatus())) {
            //存在未支付订单，直接返回
            log.info("创建订单-存在，已存在未支付订单。userId:{} productId:{} orderId:{}", shopCartReq.getUserId(), shopCartReq.getProductId(), unPaidOrder.getOrderId());
            return PayOrderRes.builder()
                    .orderId(unPaidOrder.getOrderId())
                    .build();
        }
        //2. 查询商品， 创建订单
        ProductVO productVO = productRPC.queryProductByProductId(shopCartReq.getProductId());
        String orderId = RandomStringUtils.randomNumeric(10);
        orderDao.insert(PayOrder.builder()
                        .userId(shopCartReq.getUserId())
                        .productId(shopCartReq.getProductId())
                        .productName(productVO.getProductName())
                        .orderId(orderId)
                        .orderTime(new Date())
                        .totalAmount(productVO.getPrice())
                        .status(Constants.OrderStatusEnum.CREATE.getCode())
                        .build());

        //3. 创建支付订单
        return PayOrderRes.builder()
                .orderId(orderId)
                .payUrl("null")
                .build();
    }
}
