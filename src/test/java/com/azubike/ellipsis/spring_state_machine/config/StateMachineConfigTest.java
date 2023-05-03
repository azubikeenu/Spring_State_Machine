package com.azubike.ellipsis.spring_state_machine.config;

import com.azubike.ellipsis.spring_state_machine.domain.PaymentEvents;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentState;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class StateMachineConfigTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    StateMachineFactory<PaymentState , PaymentEvents> factory;

    @Test
   void createStateMachine(){
        final StateMachine<PaymentState, PaymentEvents> stateMachine = factory.getStateMachine(UUID.randomUUID());
        stateMachine.start();
        stateMachine.sendEvent(PaymentEvents.PRE_AUTHORIZE);
        stateMachine.sendEvent(PaymentEvents.PRE_AUTH_APPROVED);
        stateMachine.sendEvent(PaymentEvents.PRE_AUTH_DECLINE);

    }

}