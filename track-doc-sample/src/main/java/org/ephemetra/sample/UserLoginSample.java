package org.ephemetra.sample;

import org.ephemetra.annotation.EventField;
import org.ephemetra.annotation.EventTrack;

import java.time.LocalDateTime;

@EventTrack(name = "user_login", desc = "用户登陆", trigger = "小程序认证登陆")
public class UserLoginSample {

    @EventField(desc = "用户名")
    private String username;

    @EventField(desc = "登陆时间")
    private LocalDateTime loginTime;
}
