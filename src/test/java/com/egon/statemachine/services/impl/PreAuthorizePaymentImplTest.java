package com.egon.statemachine.services.impl;

import com.egon.statemachine.dtos.PaymentDto;
import com.egon.statemachine.enums.PaymentStateEnum;
import com.egon.statemachine.integrations.CheckPreAuthorizeCreditIntegration;
import com.egon.statemachine.repositories.PaymentRepository;
import com.egon.statemachine.services.NewPaymentService;
import com.egon.statemachine.services.PreAuthorizePayment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class PreAuthorizePaymentImplTest {

  @Autowired
  NewPaymentService newPaymentService;

  @Autowired
  PreAuthorizePayment preAuthorizePaymentService;

  @Autowired
  PaymentRepository repository;

  @MockBean
  CheckPreAuthorizeCreditIntegration checkPreAuthorizeCreditIntegration;

  @Test
  void ShouldApprovedPreAuth() {
    when(checkPreAuthorizeCreditIntegration.execute(anyLong())).thenReturn(Boolean.TRUE);
    final var savedPayment = newPaymentService.execute(PaymentDto.builder()
        .amount(BigDecimal.TEN)
        .build());
    assertThat(savedPayment.getState()).isEqualTo(PaymentStateEnum.NEW);

    final var stateMachine = preAuthorizePaymentService.execute(savedPayment.getId());
    final var preAuthorizedPayment = repository.findById(savedPayment.getId()).orElseThrow();

    assertThat(stateMachine.getState().getId()).isEqualTo(PaymentStateEnum.PRE_AUTH);
    assertThat(preAuthorizedPayment.getState()).isEqualTo(PaymentStateEnum.PRE_AUTH);
  }

  @Test
  void ShouldDeclinedPreAuth() {
    when(checkPreAuthorizeCreditIntegration.execute(anyLong())).thenReturn(Boolean.FALSE);
    final var savedPayment = newPaymentService.execute(PaymentDto.builder()
        .amount(BigDecimal.TEN)
        .build());
    assertThat(savedPayment.getState()).isEqualTo(PaymentStateEnum.NEW);

    final var stateMachine = preAuthorizePaymentService.execute(savedPayment.getId());
    final var preAuthorizedPayment = repository.findById(savedPayment.getId()).orElseThrow();

    assertThat(stateMachine.getState().getId()).isEqualTo(PaymentStateEnum.PRE_AUTH_ERROR);
    assertThat(preAuthorizedPayment.getState()).isEqualTo(PaymentStateEnum.PRE_AUTH_ERROR);
  }
}