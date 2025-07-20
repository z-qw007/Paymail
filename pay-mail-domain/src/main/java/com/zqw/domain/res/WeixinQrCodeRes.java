package com.zqw.domain.res;

import lombok.Data;

/**
 * @Author zqw
 * @Description 获取二维码的DTO对象
 */
@Data
public class WeixinQrCodeRes {
    /**
     * 获取的二维码ticket
     */
    private String ticket;
    /**
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    private String url;
    /**
     * 二维码的有效时间，以秒为单位。
     */
    private Long expire_seconds;
}
