package com.egon.statemachine.services.impl;

import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import com.egon.statemachine.interceptors.PaymentStateChangeInterceptor;
import com.egon.statemachine.mappers.PaymentMapper;
import com.egon.statemachine.repositories.PaymentRepository;
import com.egon.statemachine.services.BasePaymentService;
import com.egon.statemachine.services.PreAuthorizePayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PreAuthorizePaymentImpl extends BasePaymentService implements PreAuthorizePayment {
  public PreAuthorizePaymentImpl(
      PaymentRepository paymentRepository,
      StateMachineFactory<PaymentStateEnum, PaymentEventEnum> stateMachineFactory,
      PaymentMapper mapper,
      PaymentStateChangeInterceptor paymentStateChangeInterceptor) {
    super(paymentRepository, stateMachineFactory, mapper, paymentStateChangeInterceptor);
  }

  @Transactional
  @Override
  public StateMachine<PaymentStateEnum, PaymentEventEnum> execute(Long paymentId) {
    final var stateMachine = build(paymentId);
    sendEvent(paymentId, stateMachine, PaymentEventEnum.PRE_AUTHORIZE);
    log.debug("Pre authorize event sent for payment {}", paymentId);

    return stateMachine;
  }
}
