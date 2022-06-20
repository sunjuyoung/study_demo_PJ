package com.demo.aop;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)//기본값 ,얼마나 유지 할 것인가
public @interface PerLogging {
}
