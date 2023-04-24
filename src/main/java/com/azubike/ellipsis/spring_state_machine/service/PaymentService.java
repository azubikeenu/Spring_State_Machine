package com.azubike.ellipsis.spring_state_machine.service;

import com.azubike.ellipsis.spring_state_machine.domain.Payment;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentEvents;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {
    Payment newPayment(Payment payment);

    StateMachine<PaymentState, PaymentEvents> preAuthorize(long paymentId);

    StateMachine<PaymentState, PaymentEvents> authorize(long paymentId);

    StateMachine<PaymentState, PaymentEvents> delcineAuth(long paymentId);
}
