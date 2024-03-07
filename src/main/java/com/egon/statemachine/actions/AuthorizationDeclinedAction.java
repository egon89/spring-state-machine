package com.egon.statemachine.actions;

import com.egon.statemachine.StateContextHelper;
import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationDeclinedAction implements Action<PaymentStateEnum, PaymentEventEnum> {
  @Override
  public void execute(StateContext<PaymentStateEnum, PaymentEventEnum> stateContext) {
    final var paymentId = StateContextHelper.getPaymentId(stateContext);
    log.debug("Authorization declined action for payment {}", paymentId);
  }
}
