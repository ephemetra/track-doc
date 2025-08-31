package io.github.sample;

import io.github.ephemetra.annotation.EventField;
import io.github.ephemetra.annotation.EventTrack;

import java.time.LocalDateTime;

@EventTrack(name = "user_login", desc = "用户登陆", trigger = "小程序认证登陆")
public class UserLoginSample {

    @EventField(desc = "用户名")
    private String username;

    @EventField(desc = "登陆时间")
    private LocalDateTime loginTime;
}
