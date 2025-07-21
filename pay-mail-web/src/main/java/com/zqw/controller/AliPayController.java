package com.zqw.controller;

import com.zqw.common.constants.Constants;
import com.zqw.common.response.Response;
import com.zqw.controller.dto.CreatePayRequestDTO;
import com.zqw.domain.req.ShopCartReq;
import com.zqw.domain.res.PayOrderRes;
import com.zqw.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@CrossOrigin("*")
@RequestMapping("/api/v1/alipay/")
public class AliPayController {

    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;

    @Resource
    private IOrderService orderService;

    @PostMapping("create_pay_order")
    public Response<String> createPayOrder(@RequestBody CreatePayRequestDTO createPayRequestDTO) {
        String productId = createPayRequestDTO.getProductId();
        String userId = createPayRequestDTO.getUserId();

        // 下单
        try {
            PayOrderRes payOrderRes = orderService.createOrder(ShopCartReq.builder()
                    .productId(productId)
                    .userId(userId)
                    .build());

            return Response.<String>builder()
                    .data(payOrderRes.getOrderId())
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .message(Constants.ResponseCode.SUCCESS.getInfo())
                    .build();

        } catch (Exception e) {
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .message(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

}
