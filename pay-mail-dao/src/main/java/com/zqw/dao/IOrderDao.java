package com.zqw.dao;

import com.zqw.domain.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IOrderDao {

    void insert(PayOrder payOrder);

    PayOrder queryUnPayOrder(PayOrder payOrder);

    void updateOrderPayInfo(PayOrder payOrder);

    List<String> queryNoPayNotifyOrder();

    void changeOrderPaySuccess(PayOrder payOrder);

    boolean changeOrderClose(String orderId);

    List<String> queryTimeoutCloseOrderList();

}
