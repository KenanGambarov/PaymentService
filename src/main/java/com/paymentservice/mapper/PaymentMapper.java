package com.paymentservice.mapper;

import com.paymentservice.dto.request.PaymentRequestDto;
import com.paymentservice.dto.response.PaymentResponseDto;
import com.paymentservice.entity.PaymentEntity;

public class PaymentMapper {

    public static PaymentEntity toResponseDto(PaymentRequestDto requestDto){
        return PaymentEntity.builder()
                .userId(requestDto.getUserId())
                .orderId(requestDto.getOrderId())
                .totalAmount(requestDto.getAmount())
                .status(requestDto.getStatus())
                .transactionId(requestDto.getTransactionId())
                .paymentMethod(requestDto.getPaymentMethod())
                .build();
    }

    public static PaymentResponseDto toResponseDto(PaymentEntity payment){
        return PaymentResponseDto.builder()
                .amount(payment.getTotalAmount())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .paymentMethod(payment.getPaymentMethod())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public static PaymentRequestDto toRequestDto(PaymentEntity payment){
        return PaymentRequestDto.builder()
                .userId(payment.getUserId())
                .orderId(payment.getOrderId())
                .status(payment.getStatus())
                .build();
    }

}
