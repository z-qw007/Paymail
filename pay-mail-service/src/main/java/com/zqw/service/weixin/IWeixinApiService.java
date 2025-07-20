package com.zqw.service.weixin;

import com.zqw.domain.vo.WeixinTemplateMessageVO;
import com.zqw.domain.res.WeixinQrCodeRes;
import com.zqw.domain.res.WeixinTokens;
import com.zqw.domain.req.WeixinQrCodeReq;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Author zqw
 * @Description 微信API接口服务
 */
public interface IWeixinApiService {

    /**
     * 获取微信access_token
     * @param grantType 填写 client_credential
     * @param appid 账号的唯一凭证
     * @param secret 唯一凭证密钥
     * @return access_token
     */
    @GET("cgi-bin/token")
    Call<WeixinTokens> getAccessToken(@Query("grant_type") String grantType,
                                      @Query("appid") String appid,
                                      @Query("secret") String secret);

    /**
     * 创建微信二维码
     * @param accessToken 获取的 token 信息
     * @param weixinQrCodeReq 入参对象
     * @return 微信二维码
     */
    @POST("cgi-bin/qrcode/create")
    Call<WeixinQrCodeRes> createQrCode(@Query("access_token") String accessToken,
                                       @Body WeixinQrCodeReq weixinQrCodeReq);

    /**
     * 发送微信模板消息
     * @param accessToken 获取的 token 信息
     * @param weixinTemplateMessageVO 入参对象
     * @return 回复消息
     */
    @POST("cgi-bin/message/template/send")
    Call<Void> sendMessage(@Query("access_token") String accessToken,
                           @Body WeixinTemplateMessageVO weixinTemplateMessageVO);


}
