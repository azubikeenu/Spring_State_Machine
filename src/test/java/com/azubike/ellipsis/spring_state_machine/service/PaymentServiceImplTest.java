package com.azubike.ellipsis.spring_state_machine.service;

import com.azubike.ellipsis.spring_state_machine.domain.Payment;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentState;
import com.azubike.ellipsis.spring_state_machine.repo.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
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


    @Test
    void preAuthorize() {
        final Payment savedPayment = paymentService.newPayment(payment);
        assertThat(savedPayment.getPaymentState()).isEqualTo(PaymentState.NEW);
        paymentService.preAuthorize(savedPayment.getId());
        final Payment preAuthPayment = paymentRepository.findById(savedPayment.getId()).orElse(null);
        assertThat(payment).isNotNull();
        Optional.ofNullable(preAuthPayment).ifPresent(pa -> {
            assertThat(preAuthPayment.getPaymentState()).isEqualTo(PaymentState.PRE_AUTH);
        });

    }
}