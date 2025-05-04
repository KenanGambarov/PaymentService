package com.paymentservice.repository;

import com.paymentservice.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    boolean existsByOrderId(Long orderId);

}
