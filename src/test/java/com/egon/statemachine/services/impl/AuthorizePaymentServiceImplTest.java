package com.egon.statemachine.services.impl;

import com.egon.statemachine.dtos.PaymentDto;
import com.egon.statemachine.enums.PaymentStateEnum;
import com.egon.statemachine.integrations.CheckPreAuthorizeCreditIntegration;
import com.egon.statemachine.repositories.PaymentRepository;
import com.egon.statemachine.services.AuthorizePaymentService;
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
class AuthorizePaymentServiceImplTest {

  @Autowired
  NewPaymentService newPaymentService;

  @Autowired
  PreAuthorizePayment preAuthorizePaymentService;

  @Autowired
  AuthorizePaymentService authorizePaymentService;

  @Autowired
  PaymentRepository repository;

  @MockBean
  CheckPreAuthorizeCreditIntegration checkPreAuthorizeCreditIntegration;

  @Test
  void shouldApproveAuthorization() {
    when(checkPreAuthorizeCreditIntegration.execute(anyLong())).thenReturn(Boolean.TRUE);
    final var savedPayment = newPaymentService.execute(PaymentDto.builder()
        .amount(BigDecimal.TEN)
        .build());
    assertThat(savedPayment.getState()).isEqualTo(PaymentStateEnum.NEW);

    final var preAuthStateMachine = preAuthorizePaymentService.execute(savedPayment.getId());
    final var preAuthorizedPayment = repository.findById(savedPayment.getId()).orElseThrow();
    assertThat(preAuthStateMachine.getState().getId()).isEqualTo(PaymentStateEnum.PRE_AUTH);
    assertThat(preAuthorizedPayment.getState()).isEqualTo(PaymentStateEnum.PRE_AUTH);

    final var authStateMachine = authorizePaymentService.execute(savedPayment.getId());
    final var authorizedPayment = repository.findById(savedPayment.getId()).orElseThrow();
    assertThat(authStateMachine.getState().getId()).isEqualTo(PaymentStateEnum.AUTH);
    assertThat(authorizedPayment.getState()).isEqualTo(PaymentStateEnum.AUTH);
  }
}