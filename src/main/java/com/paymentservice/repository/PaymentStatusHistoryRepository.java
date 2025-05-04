package com.paymentservice.repository;

import com.paymentservice.entity.PaymentStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusHistoryRepository extends JpaRepository<PaymentStatusHistoryEntity,Long> {
}
