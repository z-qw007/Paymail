package com.zqw.domain.req;

import lombok.*;

/**
 * @Author zqw
 * @Description 获取二维码的请求对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeixinQrCodeReq {

    /**
     * 二维码类型
     */
    private String action_name;
    /**
     * 二维码参数
     */
    private ActionInfo action_info;
    /**
     * 二维码过期时间
     */
    private Long expire_seconds;

    /**
     * 二维码参数封装类
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActionInfo {
        Scene scene;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Scene {
            int scene_id;
            String scene_str;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ActionNameTypeVO {
        QR_SCENE("QR_SCENE", "临时的整型参数值"),
        QR_STR_SCENE("QR_STR_SCENE", "临时的字符串参数值"),
        QR_LIMIT_SCENE("QR_LIMIT_SCENE", "永久的整型参数值"),
        QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE", "永久的字符串参数值");

        private String code;
        private String info;
    }



}
