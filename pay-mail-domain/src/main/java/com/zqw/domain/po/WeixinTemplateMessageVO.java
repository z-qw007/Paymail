package com.zqw.domain.po;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zqw
 * @Description 微信模板消息
 */
@Data
public class WeixinTemplateMessageVO {
    /**
     * 接收者openid
     */
    private String touser = "oyNgu6jMqdxuDamutOvWWKPecQpQ";
    /**
     * 模板ID
     */
    private String template_id = "vjK_0l4tTQP6oA7xJ74iJc0TEsXcXpm3Jvp29j8FGU8";
    /**
     * 模板跳转链接
     */
    private String url = "https://weixin.qq.com";
    /**
     * 模板数据
     */
    private Map<String, Map<String, String>> data = new HashMap<>();

    public WeixinTemplateMessageVO(String touser, String template_id) {
        this.touser = touser;
        this.template_id = template_id;
    }

    /**
     * @Author zqw
     * @Description 向data中添加模板消息数据项
     * @param key
     * @param value
     */
    public void put(TemplateKey key, String value) {
        data.put(key.getCode(), new HashMap<String, String>() {
            private static final long serialVersionUID = 7092338402387318563L;

            {
                put("value", value);
            }
        });
    }

    public static void put(Map<String, Map<String, String>> data, TemplateKey key, String value) {
        data.put(key.getCode(), new HashMap<String, String>() {
            private static final long serialVersionUID = 7092338402387318563L;

            {
                put("value", value);
            }
        });
    }

    /**
     * @Author zqw
     * @Description 模板消息数据项
     */
    public enum TemplateKey {
        USER("userName", "用户名");

        private String code;
        private String desc;

        TemplateKey(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

}
