package com.zqw.domain.res;

import com.zqw.common.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayOrderRes {

    // 用户ID
    private String userId;
    // 订单ID
    private String orderId;
    // 支付链接
    private String payUrl;
    // 订单状态枚举
    private Constants.OrderStatusEnum orderStatusEnum;

}
