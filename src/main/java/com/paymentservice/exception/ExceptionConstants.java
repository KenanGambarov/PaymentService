package com.paymentservice.exception;

import lombok.Getter;

@Getter
public enum ExceptionConstants {

    VALIDATION_FAILED("Validation Failed"),
    UNEXPECTED_ERROR("Unexpected Error"),
    CLIENT_ERROR("Exception from Client"),
    INVALID_ORDER("Invalid order for payment"),
    PAYMENT_NOT_FOUND("Payment Not Found"),
    ACCESS_DENIED("Order does not belong to this user"),
    PAYMENT_STATUS("This payment status is already %s"),
    AMOUNT_DIFFERENCE("Amount difference between payment and order"),
    ORDER_PAYMENT_EXISTS("Payment already exists for this order");

    private final String message;

    ExceptionConstants(String message) {
        this.message = message;
    }

    public String getMessagePattern(Object... args) {
        return String.format(this.message, args);
    }
}
