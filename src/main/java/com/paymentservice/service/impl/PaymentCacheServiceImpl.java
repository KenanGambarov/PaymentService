package com.paymentservice.service.impl;

import com.paymentservice.entity.PaymentEntity;
import com.paymentservice.repository.PaymentRepository;
import com.paymentservice.service.PaymentCacheService;
import com.paymentservice.util.CacheUtil;
import com.paymentservice.util.constraints.PaymentCacheConstraints;
import com.paymentservice.util.constraints.PaymentCacheDurationConstraints;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentCacheServiceImpl implements PaymentCacheService {

    private final PaymentRepository orderRepository;
    private final CacheUtil cacheUtil;

    @Override
    @CircuitBreaker(name = "redisBreaker", fallbackMethod = "fallbackPaymentCache")
    @Retry(name = "redisRetry", fallbackMethod = "fallbackPaymentCache")
    public  Optional<PaymentEntity> getPaymentFromCacheOrDB(Long paymentId) {
        PaymentEntity paymentEntity = cacheUtil.getOrLoad(PaymentCacheConstraints.PAYMENT_KEY.getKey(paymentId),
                () -> orderRepository.findById(paymentId).orElse(null),
                PaymentCacheDurationConstraints.DAY.toDuration());
        return Optional.ofNullable(paymentEntity);
    }

    public Optional<PaymentEntity> fallbackPaymentCache(Long paymentId, Throwable t) {
        log.error("Redis not available for paymentId {}, falling back to DB. Error: {}",paymentId, t.getMessage());
        return  Optional.empty();
    }

    @Override
    @CircuitBreaker(name = "redisBreaker", fallbackMethod = "fallbackClearPaymentCache")
    @Retry(name = "redisRetry", fallbackMethod = "fallbackClearPaymentCache")
    public void clearPaymentCache(Long paymentId) {
        cacheUtil.deleteFromCache(PaymentCacheConstraints.PAYMENT_KEY.getKey(paymentId));
        log.debug("Cache cleared for paymentId {}",  paymentId);
    }

    public void fallbackClearPaymentCache(Long paymentId, Throwable t) {
        log.warn("Redis not available to clear cache for paymentId {}, ignoring. Error: {}", paymentId, t.getMessage());
    }

}
