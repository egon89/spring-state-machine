package com.egon.statemachine.services.impl;

import com.egon.statemachine.dtos.PaymentDto;
import com.egon.statemachine.enums.PaymentStateEnum;
import com.egon.statemachine.repositories.PaymentRepository;
import com.egon.statemachine.services.NewPaymentService;
import com.egon.statemachine.services.PreAuthorizePayment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PreAuthorizePaymentImplTest {

  @Autowired
  NewPaymentService newPaymentService;

  @Autowired
  PreAuthorizePayment preAuthorizePaymentService;

  @Autowired
  PaymentRepository repository;

  @Test
  void preAuth() {
    final var savedPayment = newPaymentService.execute(PaymentDto.builder()
        .amount(BigDecimal.TEN)
        .build());
    assertThat(savedPayment.getState()).isEqualTo(PaymentStateEnum.NEW);

    final var stateMachine = preAuthorizePaymentService.execute(savedPayment.getId());
    final var preAuthorizedPayment = repository.findById(savedPayment.getId()).orElseThrow();

    assertThat(stateMachine.getState().getId()).isEqualTo(PaymentStateEnum.PRE_AUTH);
    assertThat(preAuthorizedPayment.getState()).isEqualTo(PaymentStateEnum.PRE_AUTH);
  }
}