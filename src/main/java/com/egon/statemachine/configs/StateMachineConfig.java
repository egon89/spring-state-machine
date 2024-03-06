package com.egon.statemachine.configs;

import com.egon.statemachine.actions.PreAuthorizeCreditAction;
import com.egon.statemachine.actions.ActionUtil;
import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;
import java.util.Optional;

@Slf4j
@EnableStateMachineFactory
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentStateEnum, PaymentEventEnum> {

  @Autowired
  private PreAuthorizeCreditAction preAuthorizeCreditAction;

  @Override
  public void configure(StateMachineStateConfigurer<PaymentStateEnum, PaymentEventEnum> states) throws Exception {
    states.withStates()
        .initial(PaymentStateEnum.NEW)
        .states(EnumSet.allOf(PaymentStateEnum.class))
        .end(PaymentStateEnum.AUTH)
        .end(PaymentStateEnum.PRE_AUTH_ERROR)
        .end(PaymentStateEnum.AUTH_ERROR);
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<PaymentStateEnum, PaymentEventEnum> transitions) throws Exception {
    transitions
        .withExternal()
        .source(PaymentStateEnum.NEW).target(PaymentStateEnum.NEW).event(PaymentEventEnum.PRE_AUTHORIZE)
          .action(ActionUtil.functionalAction)
          .action(preAuthorizeCreditAction)
        .and()
        .withExternal()
        .source(PaymentStateEnum.NEW).target(PaymentStateEnum.PRE_AUTH).event(PaymentEventEnum.PRE_AUTH_APPROVED)
        .and()
        .withExternal()
        .source(PaymentStateEnum.NEW).target(PaymentStateEnum.PRE_AUTH_ERROR).event(PaymentEventEnum.PRE_AUTH_DECLINED);
  }

  @Override
  public void configure(StateMachineConfigurationConfigurer<PaymentStateEnum, PaymentEventEnum> config) throws Exception {
    config.withConfiguration()
        .autoStartup(true)
        .listener(new StateMachineListenerAdapter<>(){
      @Override
      public void stateChanged(State<PaymentStateEnum, PaymentEventEnum> from, State<PaymentStateEnum, PaymentEventEnum> to) {
        log.info("state changed from {} to {}",
            Optional.ofNullable(from).map(State::getId).map(Enum::toString).orElse("-"),
            Optional.ofNullable(to).map(State::getId).map(Enum::toString).orElse("-"));
      }
    });
  }
}
