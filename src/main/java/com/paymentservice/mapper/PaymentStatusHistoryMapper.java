package com.paymentservice.mapper;

import com.paymentservice.dto.enums.PaymentStatus;
import com.paymentservice.entity.PaymentEntity;
import com.paymentservice.entity.PaymentStatusHistoryEntity;

public class PaymentStatusHistoryMapper {

    public static PaymentStatusHistoryEntity toEntity(PaymentEntity payment, PaymentStatus status) {
        return PaymentStatusHistoryEntity.builder()
                .paymentId(payment)
                .oldStatus(status)
                .newStatus(payment.getStatus())
                .build();
    }

}
