package com.zqw.service;

import java.io.IOException;

public interface ILoginService {
    /**
     * 获取微信登录二维码
     * @return 二维码
     */
    String createQrCodeTicket() throws Exception;

    /**
     * 检查登录状态
     * @param ticket 二维码
     * @return 登录状态
     */
    String checkLogin(String ticket);

    /**
     * 保存登录状态
     * @param ticket 二维码
     * @param openid 用户标识
     * @throws IOException
     */
    void saveLoginState(String ticket, String openid) throws IOException;
}
