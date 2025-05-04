package com.paymentservice.service.impl;

import com.paymentservice.client.OrderServiceClient;
import com.paymentservice.dto.enums.OrderStatus;
import com.paymentservice.dto.enums.PaymentStatus;
import com.paymentservice.dto.enums.RabbitQueueType;
import com.paymentservice.dto.request.PaymentRequestDto;
import com.paymentservice.dto.request.PaymentStatusUpdateDto;
import com.paymentservice.dto.response.OrderResponseDto;
import com.paymentservice.dto.response.PaymentResponseDto;
import com.paymentservice.entity.PaymentEntity;
import com.paymentservice.exception.BadRequestException;
import com.paymentservice.exception.ExceptionConstants;
import com.paymentservice.exception.NotFoundException;
import com.paymentservice.mapper.PaymentMapper;
import com.paymentservice.mapper.PaymentStatusHistoryMapper;
import com.paymentservice.queue.QueueSender;
import com.paymentservice.repository.PaymentRepository;
import com.paymentservice.repository.PaymentStatusHistoryRepository;
import com.paymentservice.service.PaymentCacheService;
import com.paymentservice.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStatusHistoryRepository statusHistoryRepository;
    private final PaymentCacheService paymentCacheService;
    private final OrderServiceClient orderServiceClient;
    private final QueueSender queueSender;

    @Override
    public PaymentResponseDto getPaymentById(Long paymentId) {
        PaymentEntity paymentEntity = paymentCacheService.getPaymentFromCacheOrDB(paymentId)
                .orElseThrow(() -> new NotFoundException(ExceptionConstants.PAYMENT_NOT_FOUND.getMessage()));
        return PaymentMapper.toResponseDto(paymentEntity);
    }

    @Transactional
    @Override
    public void createPayment(PaymentRequestDto requestDto) {
        OrderResponseDto order = orderServiceClient.getOrderById(requestDto.getOrderId());

        if (!OrderStatus.PENDING.equals(order.getStatus())) {
            throw new BadRequestException(ExceptionConstants.INVALID_ORDER.getMessage());
        }

        if (!order.getUserId().equals(requestDto.getUserId())) {
            throw new BadRequestException(ExceptionConstants.ACCESS_DENIED.getMessage());
        }

        if (paymentRepository.existsByOrderId(requestDto.getOrderId())) {
            throw new BadRequestException(ExceptionConstants.ORDER_PAYMENT_EXISTS.getMessage());
        }
        log.info("Creating payment for totalAmount={}, order.getTotalAmount()={}", requestDto.getAmount(),order.getTotalAmount());
        if(requestDto.getAmount().compareTo(order.getTotalAmount())!=0){
            throw new BadRequestException(ExceptionConstants.AMOUNT_DIFFERENCE.getMessage());
        }

        log.info("Creating payment for orderId={} by userId={}", requestDto.getOrderId(), requestDto.getUserId());
        PaymentEntity payment = paymentRepository.save(PaymentMapper.toResponseDto(requestDto));
        saveOrderHistory(payment,null);
        paymentCacheService.clearPaymentCache(payment.getId());
    }

    @Transactional
    @Override
    public void updatePaymentStatus(Long paymentId, PaymentStatusUpdateDto statusUpdateDto) {
        PaymentEntity paymentEntity = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException(ExceptionConstants.PAYMENT_NOT_FOUND.getMessage()));

        if (statusUpdateDto.getStatus().equals(paymentEntity.getStatus())) {
            throw new BadRequestException(ExceptionConstants.PAYMENT_STATUS.getMessagePattern(statusUpdateDto.getStatus()));
        }
        paymentEntity.setStatus(statusUpdateDto.getStatus());
        paymentRepository.save(paymentEntity);
        saveOrderHistory(paymentEntity,statusUpdateDto.getStatus());
        paymentCacheService.clearPaymentCache(paymentEntity.getId());
        queueSender.sendOrderUpdate(RabbitQueueType.QUEUE_NAME.getQueueName(),PaymentMapper.toRequestDto(paymentEntity));

    }

    private void saveOrderHistory(PaymentEntity payment, PaymentStatus status) {
        statusHistoryRepository.save(
                PaymentStatusHistoryMapper.toEntity(payment,status)
        );
        log.info("PaymentStatusHistory saved. paymentId: {}, paymentStatus: {}",
                payment.getId(), payment.getStatus());
    }
}
