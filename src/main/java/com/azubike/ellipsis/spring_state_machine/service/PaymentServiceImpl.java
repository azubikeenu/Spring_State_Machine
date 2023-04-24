package com.azubike.ellipsis.spring_state_machine.service;

import com.azubike.ellipsis.spring_state_machine.domain.Payment;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentEvents;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentState;
import com.azubike.ellipsis.spring_state_machine.listeners.PaymentStateChangeInterceptor;
import com.azubike.ellipsis.spring_state_machine.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    public final static String PAYMENT_ID = "payment_id";
    private final PaymentRepository paymentRepository;

    private final PaymentStateChangeInterceptor paymentStateChangeInterceptor;
    private final StateMachineFactory<PaymentState ,PaymentEvents> factory;
    @Override
    public Payment newPayment(final Payment payment) {
         payment.setPaymentState(PaymentState.NEW);
        return paymentRepository.save(payment);
    }

    @Override
    public StateMachine<PaymentState, PaymentEvents> preAuthorize(final long paymentId) {
        var sm = build(paymentId);
        sendEvent(paymentId , sm , PaymentEvents.PRE_AUTH_APPROVED);
        return sm;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvents> authorize(final long paymentId) {
        var sm = build(paymentId);
        sendEvent(paymentId , sm , PaymentEvents.AUTH_APPROVED);
        return sm;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvents> delcineAuth(final long paymentId) {
        var sm = build(paymentId);
        sendEvent(paymentId , sm , PaymentEvents.AUTH_DECLINED);
        return sm;
    }

    private void sendEvent(long paymentId , StateMachine<PaymentState ,PaymentEvents> sm , PaymentEvents event){
        final Message<PaymentEvents> message = MessageBuilder
                .withPayload(event)
                .setHeader(PAYMENT_ID, paymentId)
                .build();
        sm.sendEvent(message);
    }

    //TODO make state machine reactive/non-blocking
    private StateMachine<PaymentState ,PaymentEvents> build(long paymentId){
        final Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));
        final StateMachine<PaymentState, PaymentEvents> sm = factory.getStateMachine(Long.toString(payment.getId()));
        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions( sma -> {
            sma.addStateMachineInterceptor(paymentStateChangeInterceptor);
          sma.resetStateMachine(new DefaultStateMachineContext<>(payment.getPaymentState() , null , null, null));
        });
        sm.start();
        return sm;
    };
}
