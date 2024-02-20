package com.egon.statemachine.services;

import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import org.springframework.statemachine.StateMachine;

public interface PreAuthorizePayment {
  StateMachine<PaymentStateEnum, PaymentEventEnum> execute(Long paymentId);
}
