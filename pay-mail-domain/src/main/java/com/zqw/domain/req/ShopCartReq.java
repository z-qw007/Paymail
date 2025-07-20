package com.zqw.domain.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopCartReq {

    // The ID of the product
    private String productId;
    // The ID of the user
    private String userId;

}
