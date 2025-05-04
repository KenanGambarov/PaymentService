package com.paymentservice.client;

import com.paymentservice.client.decoder.ClientServiceErrorDecoder;
import com.paymentservice.dto.response.OrderResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "${client.order-service.url}",
        path = "/internal/v1/order",configuration = ClientServiceErrorDecoder.class)
public interface OrderServiceClient {

    @GetMapping("/{id}")
    OrderResponseDto getOrderById(@PathVariable("id") Long id);

}
