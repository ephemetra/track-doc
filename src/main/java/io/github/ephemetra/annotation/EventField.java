package io.github.ephemetra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface EventField {

    /**
     * 事件字段名
     */
    String name() default "";

    /**
     * 事件字段描述
     */
    String desc() default "";
}
