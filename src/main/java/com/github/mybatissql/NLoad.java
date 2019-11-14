package com.github.mybatissql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* 定义字段的注解*/
/*该注解只能用在成员变量上*/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NLoad {}
