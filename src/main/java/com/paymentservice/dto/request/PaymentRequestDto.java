package com.paymentservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paymentservice.dto.enums.PaymentMethod;
import com.paymentservice.dto.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequestDto {

    private Long userId;

    private Long orderId;

    private BigDecimal amount;

    private PaymentStatus status;

    private PaymentMethod paymentMethod;

    private String transactionId;

}
