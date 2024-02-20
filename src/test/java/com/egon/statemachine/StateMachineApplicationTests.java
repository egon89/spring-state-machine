package com.egon.statemachine;

import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.config.StateMachineFactory;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SpringBootTest
class StateMachineApplicationTests {

  @Autowired
  StateMachineFactory<PaymentStateEnum, PaymentEventEnum> factory;

  @Test
  void testNewStateMachine() {
    final var sm = factory.getStateMachine(UUID.randomUUID());
    System.out.printf("> initial: %s%n", sm.getState().getId());

    final var preAuthorizeResult = sm.sendEventCollect(
        Mono.just(MessageBuilder.withPayload(PaymentEventEnum.PRE_AUTHORIZE).build()));
    preAuthorizeResult
        .flatMap(stateMachineEventResults -> {
          return sm.sendEventCollect(
              Mono.just(MessageBuilder.withPayload(PaymentEventEnum.PRE_AUTH_APPROVED).build()));
        })
        .subscribe();

    System.out.printf("> final: %s%n", sm.getState().getId());
  }
}
