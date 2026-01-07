package com.dreamhouse.coupon.model;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE}) // 作用在類別上
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CouponDateValidator.class)
public @interface CouponDateCheck {
    String message() default "日期必須在今日之後";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
