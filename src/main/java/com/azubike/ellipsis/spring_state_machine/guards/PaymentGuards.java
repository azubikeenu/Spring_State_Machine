package com.azubike.ellipsis.spring_state_machine.guards;


import com.azubike.ellipsis.spring_state_machine.domain.PaymentEvents;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentState;
import com.azubike.ellipsis.spring_state_machine.service.PaymentServiceImpl;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class PaymentGuards {
    public Guard<PaymentState , PaymentEvents> requirePaymentId(){
        return stateContext -> stateContext.getMessageHeader(PaymentServiceImpl.PAYMENT_ID)!=null;
    }
}
