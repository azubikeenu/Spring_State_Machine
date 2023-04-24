package com.azubike.ellipsis.spring_state_machine.listeners;

import com.azubike.ellipsis.spring_state_machine.domain.PaymentEvents;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentState;
import com.azubike.ellipsis.spring_state_machine.repo.PaymentRepository;
import com.azubike.ellipsis.spring_state_machine.service.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState , PaymentEvents> {
    private final  PaymentRepository paymentRepository;

    @Override
    public void preStateChange(final State<PaymentState, PaymentEvents> state, final Message<PaymentEvents> message,
                               final Transition<PaymentState, PaymentEvents> transition,
                               final StateMachine<PaymentState, PaymentEvents> stateMachine,
                               final StateMachine<PaymentState, PaymentEvents> rootStateMachine) {

        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable(msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID, -1L)))
                .flatMap(paymentId -> paymentRepository.findById((long) paymentId))
                .ifPresent(payment -> {
                    payment.setPaymentState(state.getId());
            paymentRepository.save(payment);
        });

    }
}
