package com.azubike.ellipsis.spring_state_machine.config;

import com.azubike.ellipsis.spring_state_machine.actions.PaymentActions;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentEvents;
import com.azubike.ellipsis.spring_state_machine.domain.PaymentState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@Slf4j
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState , PaymentEvents> {
    private final PaymentActions paymentActions ;
    @Override
    public void configure(final StateMachineStateConfigurer<PaymentState, PaymentEvents> states) throws Exception {
        states.withStates()
                .initial(PaymentState.NEW).states(EnumSet.allOf(PaymentState.class))
                .end(PaymentState.AUTH_ERROR)
                .end(PaymentState.PRE_AUTH_ERROR).end(PaymentState.AUTH);
    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<PaymentState, PaymentEvents> transitions) throws Exception {
        transitions.withExternal()
                .source(PaymentState.NEW).target(PaymentState.NEW).event(PaymentEvents.PRE_AUTHORIZE).action(paymentActions.preAuthAction())
                .and()
                .withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTH_ERROR).event(PaymentEvents.PRE_AUTH_DECLINE)
                .and()
                .withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTH).event(PaymentEvents.PRE_AUTH_APPROVED);
    }


    @Override
    public void configure(final StateMachineConfigurationConfigurer<PaymentState, PaymentEvents> config) throws Exception {
        StateMachineListenerAdapter<PaymentState , PaymentEvents> adapter = new StateMachineListenerAdapter<>(){
            @Override
            public void stateChanged(final State<PaymentState, PaymentEvents> from, final State<PaymentState, PaymentEvents> to) {
              log.info("State changed from :{} to {}", from , to );
            }
        };
        config.withConfiguration().listener(adapter);
    }
}
