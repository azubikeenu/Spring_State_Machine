package com.azubike.ellipsis.spring_state_machine.actions;

import com.azubike.ellipsis.spring_state_machine.domain.PaymentEvents;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentState;
import com.azubike.ellipsis.spring_state_machine.service.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor

public class PaymentActions {

    public Action<PaymentState, PaymentEvents> preAuthAction() {
        return stateContext -> {
            log.debug("Firing preAuthAction");
            if (ThreadLocalRandom.current().nextInt(10) > 2) {
                log.debug("PreAuthorization Approved");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvents.PRE_AUTH_APPROVED)
                        .setHeader(PaymentServiceImpl.PAYMENT_ID, stateContext.getMessageHeader(PaymentServiceImpl.PAYMENT_ID)).build());
            } else {
                log.debug("PreAuthorization Declined!!!");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvents.PRE_AUTH_DECLINE)
                        .setHeader(PaymentServiceImpl.PAYMENT_ID, stateContext.getMessageHeader(PaymentServiceImpl.PAYMENT_ID)).build());
            }
        };
    }
}
