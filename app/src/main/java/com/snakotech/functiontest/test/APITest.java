package com.snakotech.functiontest.test;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Created by Administrator on 2017/8/12.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface APITest{
    //api的名称
    public String name() default "";
    //api的参数,竖线分割
    public String param() default "";
    //api的测试数据,JsonArray形式，内容是JsonObject
    public String testParam() default "";
}
