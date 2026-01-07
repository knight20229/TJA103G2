package com.dreamhouse.coupon.model;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CouponDateValidator implements ConstraintValidator<CouponDateCheck, CouponVO> {

    @Override
    public boolean isValid(CouponVO coupon, ConstraintValidatorContext context) {
        // 如果 send_time 不為 null，代表已經發送或是排程在跑，我們「不做檢查」，直接回傳 true
        if (coupon.getSendTime() != null) {
            return true;
        }

        // 如果 send_time 為 null，則執行日期檢查
        LocalDate today = LocalDate.now();
        boolean isStartFuture = coupon.getStartDt() != null && coupon.getStartDt().isAfter(today);
        boolean isEndFuture = coupon.getEndDt() != null && coupon.getEndDt().isAfter(today);

        // 如果檢查失敗，你可以自定義錯誤訊息掛在特定欄位上
        if (!(isStartFuture && isEndFuture)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("日期必須在今日(不含)之後")
            .addPropertyNode("startDt").addConstraintViolation();
            context.buildConstraintViolationWithTemplate("日期必須在今日(不含)之後")
                   .addPropertyNode("endDt").addConstraintViolation();
            return false;
        }

        return true;
    }
}