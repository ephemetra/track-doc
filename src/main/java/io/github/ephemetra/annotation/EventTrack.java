package io.github.ephemetra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface EventTrack {

    /**
     * 事件名称
     */
    String name() default "";

    /**
     * 事件描述
     */
    String desc() default "";

    /**
     * 触发时机
     */
    String trigger() default "";
}
