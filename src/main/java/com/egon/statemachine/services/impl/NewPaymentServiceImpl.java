package com.egon.statemachine.services.impl;

import com.egon.statemachine.dtos.PaymentDto;
import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import com.egon.statemachine.mappers.PaymentMapper;
import com.egon.statemachine.repositories.PaymentRepository;
import com.egon.statemachine.services.NewPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NewPaymentServiceImpl implements NewPaymentService {

  private final PaymentRepository paymentRepository;

  private final StateMachineFactory<PaymentStateEnum, PaymentEventEnum> stateMachineFactory;

  private final PaymentMapper mapper;

  @Override
  public PaymentDto execute(PaymentDto payment) {
    payment.setState(PaymentStateEnum.NEW);
    final var paymentToSave = mapper.toEntity(payment);

    return mapper.toDto(paymentRepository.save(paymentToSave));
  }
}
