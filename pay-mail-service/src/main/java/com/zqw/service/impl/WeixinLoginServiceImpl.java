package com.zqw.service.impl;

import com.google.common.cache.Cache;
import com.zqw.domain.vo.WeixinTemplateMessageVO;
import com.zqw.domain.req.WeixinQrCodeReq;
import com.zqw.domain.res.WeixinQrCodeRes;
import com.zqw.domain.res.WeixinTokens;
import com.zqw.service.ILoginService;
import com.zqw.service.weixin.IWeixinApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinLoginServiceImpl implements ILoginService {

    /**
     * 配置文件获取数据
     */
    @Value("${weixin.config.app-id}")
    private String appId;
    @Value("${weixin.config.app-secret}")
    private String appSecret;
    @Value("${weixin.config.template_id}")
    private String templateId;

    /**
     *
     */
    @Resource
    private IWeixinApiService weixinApiService;
    @Resource
    private Cache<String, String> weixinAccessToken;
    @Resource
    private Cache<String, String> openidToken;

    @Override
    public String createQrCodeTicket() throws Exception {
        // 1.获取 access_token
        String accessToken = weixinAccessToken.getIfPresent(appId);
        if (accessToken == null) {
            Call<WeixinTokens> call = weixinApiService.getAccessToken("client_credential", appId, appSecret);
            WeixinTokens weixinTokens = call.execute().body();
            if (weixinTokens != null) {
                accessToken = weixinTokens.getAccess_token();
                weixinAccessToken.put(appId, accessToken);
            } else {
                throw new Exception("获取 access_token 失败");
            }
        }

        // 2.创建二维码
        WeixinQrCodeReq weixinQrCodeReq = WeixinQrCodeReq.builder()
                .expire_seconds(7200L)
                .action_name(WeixinQrCodeReq.ActionNameTypeVO.QR_SCENE.getCode())
                .action_info(WeixinQrCodeReq.ActionInfo.builder()
                        .scene(WeixinQrCodeReq.ActionInfo.Scene.builder()
                                .scene_id(123)
                                .build())
                        .build())
                .build();

        Call<WeixinQrCodeRes> qrCode = weixinApiService.createQrCode(accessToken, weixinQrCodeReq);
        WeixinQrCodeRes weixinQrCodeRes = qrCode.execute().body();
        if (weixinQrCodeRes != null) {
            return weixinQrCodeRes.getTicket();
            } else {
                throw new Exception("创建二维码失败");
        }
    }

    @Override
    public String checkLogin(String ticket) {
        return openidToken.getIfPresent(ticket);
    }

    @Override
    public void saveLoginState(String ticket, String openid) throws IOException {
        openidToken.put(ticket, openid);

        // 1. 获取 accessToken 【实际业务场景，按需处理下异常】
        String accessToken = weixinAccessToken.getIfPresent(appId);
        if (null == accessToken){
            Call<WeixinTokens> call = weixinApiService.getAccessToken("client_credential", appId, appSecret);
            WeixinTokens weixinTokenRes = call.execute().body();
            assert weixinTokenRes != null;
            accessToken = weixinTokenRes.getAccess_token();
            weixinAccessToken.put(appId, accessToken);
        }

        // 2. 发送模板消息
        Map<String, Map<String, String>> data = new HashMap<>();
        WeixinTemplateMessageVO.put(data, WeixinTemplateMessageVO.TemplateKey.USER, openid);

        WeixinTemplateMessageVO templateMessageDTO = new WeixinTemplateMessageVO(openid, templateId);
        templateMessageDTO.setUrl("https://zqw.1111.com");
        templateMessageDTO.setData(data);

        Call<Void> call = weixinApiService.sendMessage(accessToken, templateMessageDTO);
        call.execute();

    }
}
