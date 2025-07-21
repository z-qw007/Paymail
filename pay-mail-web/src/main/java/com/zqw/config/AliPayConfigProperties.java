package com.zqw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "alipay", ignoreInvalidFields = true)
public class AliPayConfigProperties {

    // Application ID
    private String appId;
    // Merchant private key
    private String merchantPrivateKey;
    // Alipay public key
    private String alipayPublicKey;
    // Notify URL
    private String notifyUrl;
    // Return URL
    private String returnUrl;
    // Gateway URL
    private String gatewayUrl;
    // Sign type
    private String signType = "RSA2";
    // Charset
    private String charset = "UTF-8";
    // Format
    private String format = "JSON";
}
