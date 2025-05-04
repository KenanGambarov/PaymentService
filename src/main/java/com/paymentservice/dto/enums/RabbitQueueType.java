package com.paymentservice.dto.enums;

import lombok.Getter;

@Getter
public enum RabbitQueueType {

    QUEUE_NAME("PAYMENT_UPDATE");

    private final String queueName;

    RabbitQueueType(String queueName){
        this.queueName = queueName;
    }

}
