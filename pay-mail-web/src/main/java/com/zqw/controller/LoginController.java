package com.zqw.controller;

import com.zqw.common.constants.Constants;
import com.zqw.common.response.Response;
import com.zqw.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@CrossOrigin("*")
@RequestMapping("/api/v1/login/")
public class LoginController {

    @Resource
    private ILoginService loginService;

    @GetMapping(value = "weixin_qrcode_ticket")
    public Response<String> loginByQrCode() throws Exception {

        String qrCodeTicket = loginService.createQrCodeTicket();
        log.info("二维码ticket:{}",qrCodeTicket);
        return Response.<String>builder()
                .data(qrCodeTicket)
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .message(Constants.ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @GetMapping(value = "check_login")
    public Response<String> checkLogin(@RequestParam String ticket) {
        try {
            String openidToken = loginService.checkLogin(ticket);
            log.info("扫码检测登录结果 ticket:{} openidToken:{}", ticket, openidToken);
            if (StringUtils.isNotBlank(openidToken)) {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.SUCCESS.getCode())
                        .message(Constants.ResponseCode.SUCCESS.getInfo())
                        .data(openidToken)
                        .build();
            } else {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.NO_LOGIN.getCode())
                        .message(Constants.ResponseCode.NO_LOGIN.getInfo())
                        .build();
            }
        } catch (Exception e) {
            log.error("扫码检测登录结果失败 ticket:{}", ticket, e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .message(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
