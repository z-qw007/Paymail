package com.zqw.service;

import com.zqw.domain.res.PayOrderRes;
import com.zqw.domain.req.ShopCartReq;

public interface IOrderService {

    PayOrderRes createOrder(ShopCartReq shopCartReq) throws Exception;

}
