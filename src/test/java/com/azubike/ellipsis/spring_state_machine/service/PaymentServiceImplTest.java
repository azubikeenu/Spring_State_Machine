package com.azubike.ellipsis.spring_state_machine.service;

import com.azubike.ellipsis.spring_state_machine.domain.Payment;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentState;
import com.azubike.ellipsis.spring_state_machine.repo.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {
    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentRepository paymentRepository;
    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }


    @RepeatedTest(10)
    void preAuthorize() {
        final Payment savedPayment = paymentService.newPayment(payment);
        assertThat(savedPayment.getPaymentState()).isEqualTo(PaymentState.NEW);
        paymentService.preAuthorize(savedPayment.getId());
        final Payment preAuthPayment = paymentRepository.findById(savedPayment.getId()).orElse(null);
        assertThat(preAuthPayment).isNotNull();
        assertThat(preAuthPayment.getPaymentState()).isIn(EnumSet.of(PaymentState.PRE_AUTH , PaymentState.PRE_AUTH_ERROR));

    }
}