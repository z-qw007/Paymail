package com.zqw.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrder {
    //订单ID
    private Long id;
    //用户ID
    private String userId;
    //产品ID
    private String productId;
    //产品名称
    private String productName;
    //订单ID
    private String orderId;
    //订单时间
    private Date orderTime;
    //总金额
    private BigDecimal totalAmount;
    //状态
    private String status;
    //支付链接
    private String payUrl;
    //支付时间
    private Date payTime;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}
