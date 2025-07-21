package com.zqw.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliPayConfigProperties.class)
public class AliPayConfig {

    @Bean("alipayClient")
    public AlipayClient alipayClient(AliPayConfigProperties aliPayConfigProperties) {
        return new DefaultAlipayClient(aliPayConfigProperties.getGatewayUrl(),
                                       aliPayConfigProperties.getAppId(),
                                       aliPayConfigProperties.getMerchantPrivateKey(),
                                       aliPayConfigProperties.getFormat(),
                                       aliPayConfigProperties.getCharset(),
                                       aliPayConfigProperties.getAlipayPublicKey(),
                                       aliPayConfigProperties.getSignType()
        );
    }

}
