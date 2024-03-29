package com.egon.statemachine.services.impl;

import com.egon.statemachine.dtos.PaymentDto;
import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import com.egon.statemachine.interceptors.PaymentStateChangeInterceptor;
import com.egon.statemachine.mappers.PaymentMapper;
import com.egon.statemachine.repositories.PaymentRepository;
import com.egon.statemachine.services.BasePaymentService;
import com.egon.statemachine.services.NewPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NewPaymentServiceImpl extends BasePaymentService implements NewPaymentService {
  public NewPaymentServiceImpl(
      PaymentRepository paymentRepository,
      StateMachineFactory<PaymentStateEnum, PaymentEventEnum> stateMachineFactory,
      PaymentMapper mapper,
      PaymentStateChangeInterceptor paymentStateChangeInterceptor) {
    super(paymentRepository, stateMachineFactory, mapper, paymentStateChangeInterceptor);
  }

  @Override
  public PaymentDto execute(PaymentDto payment) {
    payment.setState(PaymentStateEnum.NEW);
    final var paymentToSave = mapper.toEntity(payment);
    final var savedPayment = paymentRepository.save(paymentToSave);
    log.debug("Payment with id {} saved ({})", savedPayment.getId(), savedPayment.getState());

    return mapper.toDto(savedPayment);
  }
}
