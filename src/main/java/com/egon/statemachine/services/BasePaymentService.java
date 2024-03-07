package com.egon.statemachine.services;

import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import com.egon.statemachine.interceptors.PaymentStateChangeInterceptor;
import com.egon.statemachine.mappers.PaymentMapper;
import com.egon.statemachine.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public abstract class BasePaymentService {
  public static final String PAYMENT_ID_HEADER = "payment_id";

  protected final PaymentRepository paymentRepository;

  protected final StateMachineFactory<PaymentStateEnum, PaymentEventEnum> stateMachineFactory;

  protected final PaymentMapper mapper;

  private final PaymentStateChangeInterceptor paymentStateChangeInterceptor;

  protected void sendEvent(
      Long paymentId,
      StateMachine<PaymentStateEnum, PaymentEventEnum> stateMachine,
      PaymentEventEnum event) {
    final var msg = MessageBuilder.withPayload(event)
        .setHeader(PAYMENT_ID_HEADER, paymentId)
        .build();
    stateMachine.sendEvent(Mono.just(msg)).blockFirst();
  }

  protected StateMachine<PaymentStateEnum, PaymentEventEnum> build(Long paymentId) {
    final var payment = paymentRepository.findById(paymentId).orElseThrow();
    final var stateMachine = stateMachineFactory.getStateMachine(Long.toString((payment.getId())));
    stateMachine.stopReactively().block();
    stateMachine.getStateMachineAccessor()
        .doWithAllRegions(sma -> {
          sma.addStateMachineInterceptor(paymentStateChangeInterceptor);
          sma.resetStateMachineReactively(new DefaultStateMachineContext<>(payment.getState(), null, null, null)).block();
        });
    stateMachine.startReactively().block();
    return stateMachine;
  }
}
