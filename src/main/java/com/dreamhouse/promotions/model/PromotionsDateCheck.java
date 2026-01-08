package com.dreamhouse.promotions.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dreamhouse.coupon.model.CouponDateValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE}) // 作用在類別上
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PromotionsDateValidator.class)
public @interface PromotionsDateCheck {
	String message() default "日期必須在今日之後";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
