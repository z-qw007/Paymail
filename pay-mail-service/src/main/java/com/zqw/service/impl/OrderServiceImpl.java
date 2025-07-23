package com.zqw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.request.AlipayTradePayRequest;
import com.google.common.eventbus.EventBus;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Value("${alipay.notify_url}")
    private String notifyUrl;
    @Value("${alipay.return_url}")
    private String returnUrl;

    @Resource
    private IOrderDao orderDao;

    @Resource
    private ProductRPC productRPC;

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private EventBus eventBus;

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
        } else if (unPaidOrder != null && Constants.OrderStatusEnum.CREATE.getCode().equals(unPaidOrder.getStatus())) {
            PayOrder payOrder = doPrePayOrder(unPaidOrder.getProductId(), unPaidOrder.getProductName(), unPaidOrder.getOrderId(), unPaidOrder.getTotalAmount());
            return PayOrderRes.builder()
                    .orderId(payOrder.getOrderId())
                    .payUrl(payOrder.getPayUrl())
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
        PayOrder payOrder = doPrePayOrder(unPaidOrder.getProductId(), unPaidOrder.getProductName(), unPaidOrder.getOrderId(), unPaidOrder.getTotalAmount());
        //3. 创建支付订单
        return PayOrderRes.builder()
                .orderId(orderId)
                .payUrl(payOrder.getPayUrl())
                .build();
    }

    @Override
    public void changeOrderPaySuccess(String orderId) {
        // Create a new PayOrder object
        PayOrder payOrder = new PayOrder();
        // Set the orderId of the PayOrder object
        payOrder.setOrderId(orderId);
        // Set the status of the PayOrder object to PAY_SUCCESS
        payOrder.setStatus(Constants.OrderStatusEnum.PAY_SUCCESS.getCode());
        // Call the changeOrderPaySuccess method of the orderDao object to update the order status
        orderDao.changeOrderPaySuccess(payOrder);

        // Post the PayOrder object as a JSON string to the eventBus
        eventBus.post(JSON.toJSONString(payOrder));
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return orderDao.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return orderDao.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return orderDao.changeOrderClose(orderId);
    }

    private PayOrder doPrePayOrder(String productId, String productName,
                                   String orderId, BigDecimal totalAmount) throws Exception {
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        JSONObject content = new JSONObject();
        content.put("out_trade_no", orderId);
        content.put("total_amount", totalAmount);
        content.put("subject", productName);
        content.put("product_code", "FAST_INSTANT_TRADE_PAY");

        request.setBizContent(content.toJSONString());

        String body = alipayClient.pageExecute(request).getBody();

        PayOrder payOrder = new PayOrder();
        payOrder.setOrderId(orderId);
        payOrder.setPayUrl(body);
        payOrder.setStatus(Constants.OrderStatusEnum.PAY_WAIT.getCode());

        orderDao.updateOrderPayInfo(payOrder);

        return payOrder;
    }
}
