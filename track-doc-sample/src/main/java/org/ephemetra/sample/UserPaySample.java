package org.ephemetra.sample;

import org.ephemetra.annotation.EventField;
import org.ephemetra.annotation.EventTrack;

@EventTrack(name = "user_pay", desc = "用户付款", trigger = "下单后完成订单支付")
public class UserPaySample {

    @EventField(desc = "用户名")
    private String username;

    @EventField(desc = "订单号")
    private String orderNo;

    @EventField(desc = "交易号")
    private String payNo;
}
