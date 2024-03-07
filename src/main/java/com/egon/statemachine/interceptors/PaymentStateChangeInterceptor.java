package com.egon.statemachine.interceptors;

import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import com.egon.statemachine.repositories.PaymentRepository;
import com.egon.statemachine.services.BasePaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentStateChangeInterceptor
    extends StateMachineInterceptorAdapter<PaymentStateEnum, PaymentEventEnum> {

  private final PaymentRepository repository;

  @Override
  public void preStateChange(
      State<PaymentStateEnum, PaymentEventEnum> state,
      Message<PaymentEventEnum> message,
      Transition<PaymentStateEnum, PaymentEventEnum> transition,
      StateMachine<PaymentStateEnum, PaymentEventEnum> stateMachine, StateMachine<PaymentStateEnum,
      PaymentEventEnum> rootStateMachine) {
    final var msg = Optional.ofNullable(message).orElseThrow(IllegalAccessError::new);
    Optional.ofNullable((Long) msg.getHeaders().get(BasePaymentService.PAYMENT_ID_HEADER))
        .ifPresent(paymentId -> {
          final var payment = repository.findById(paymentId).orElseThrow(() -> new RuntimeException("Not found!"));
          final var previousState = payment.getState();
          payment.setState(state.getId());
          final var savedPayment = repository.save(payment);
          log.debug("Payment {} changed from {} to {}",
              savedPayment.getId(), previousState, savedPayment.getState());
        });
  }
}
