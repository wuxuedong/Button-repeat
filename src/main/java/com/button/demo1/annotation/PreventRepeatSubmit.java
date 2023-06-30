package com.button.demo1.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreventRepeatSubmit {
    /**
     * 间隔时间(ms)，小于此时间视为重复提交
     */
    public int interval() default 40000;

    /**
     * 提示消息
     */
    public String message() default "不允许重复提交，请稍候再试";
}
