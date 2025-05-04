package com.paymentservice.controller;

import com.paymentservice.dto.request.PaymentRequestDto;
import com.paymentservice.dto.request.PaymentStatusUpdateDto;
import com.paymentservice.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/payment/")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    public void getPaymentById(@PathVariable("paymentId") Long paymentId){
        paymentService.getPaymentById(paymentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPayment(@RequestBody PaymentRequestDto requestDto){
        paymentService.createPayment(requestDto);
    }

    @PutMapping("/{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePaymentStatus(@PathVariable("paymentId") Long paymentId,@RequestBody PaymentStatusUpdateDto statusUpdateDto){
        paymentService.updatePaymentStatus(paymentId,statusUpdateDto);
    }

}
