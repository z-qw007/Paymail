package com.zqw.domain.res;

import lombok.Data;

/**
 * @Author zqw
 * @Description 获取access_token的DTO对象
 */
@Data
public class WeixinTokens {
    private String access_token;
    private Long expires_in;
    private String errcode;
    private String errmsg;
}
