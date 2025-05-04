package com.paymentservice.service;

import com.paymentservice.entity.PaymentEntity;

import java.util.Optional;

public interface PaymentCacheService {

    Optional<PaymentEntity> getPaymentFromCacheOrDB(Long paymentId);

    void clearPaymentCache(Long paymentId);


}
