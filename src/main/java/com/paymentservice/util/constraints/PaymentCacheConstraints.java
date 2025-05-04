package com.paymentservice.util.constraints;

public enum PaymentCacheConstraints {

    PAYMENT_KEY("ms-payment:payment:%s"),
    PAYMENT_ITEMS_KEY("ms-payment:payment-items:%s");

    private final String keyFormat;

    PaymentCacheConstraints(String keyFormat) {
        this.keyFormat = keyFormat;
    }

    public String getKey(Object... args) {
        return String.format(this.keyFormat, args);
    }
}
