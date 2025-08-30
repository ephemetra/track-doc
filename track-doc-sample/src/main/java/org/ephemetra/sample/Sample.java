package org.ephemetra.sample;

import org.ephemetra.annotation.EventTrack;

public class Sample {

    @EventTrack(eventName = "login_click", description = "用户点击了登录按钮: ${eventName}")
    public void login() {
        // ...
    }

    @EventTrack(eventName = "pay_success", description = "支付成功事件: ${eventName}")
    public void pay() {
        // ...
    }
}
