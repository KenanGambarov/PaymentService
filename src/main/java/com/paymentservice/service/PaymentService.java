package com.paymentservice.service;

import com.paymentservice.dto.enums.PaymentStatus;
import com.paymentservice.dto.request.PaymentRequestDto;
import com.paymentservice.dto.request.PaymentStatusUpdateDto;
import com.paymentservice.dto.response.PaymentResponseDto;

public interface PaymentService {

    PaymentResponseDto getPaymentById(Long paymentId);

    void createPayment(PaymentRequestDto requestDto);

    void updatePaymentStatus(Long paymentId, PaymentStatusUpdateDto statusUpdateDto);

}
