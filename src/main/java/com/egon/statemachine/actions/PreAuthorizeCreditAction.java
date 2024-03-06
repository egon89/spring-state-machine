package com.egon.statemachine.actions;

import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import com.egon.statemachine.integrations.CheckPreAuthorizeCreditIntegration;
import com.egon.statemachine.services.BasePaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class PreAuthorizeCreditAction implements Action<PaymentStateEnum, PaymentEventEnum> {

  private final CheckPreAuthorizeCreditIntegration checkPreAuthorizeCredit;

  @Override
  public void execute(StateContext<PaymentStateEnum, PaymentEventEnum> stateContext) {
    final var paymentId = (Long) stateContext.getMessageHeader(BasePaymentService.PAYMENT_ID_HEADER);
    if (checkPreAuthorizeCredit.execute(paymentId)) {
      log.debug("PreAuthorizeAction: approved for payment {}", paymentId);
      stateContext.getStateMachine().sendEvent(
          Mono.just(
              MessageBuilder.withPayload(PaymentEventEnum.PRE_AUTH_APPROVED)
                  .setHeader(BasePaymentService.PAYMENT_ID_HEADER, paymentId).build())).subscribe();
    } else {
      log.debug("PreAuthorizeAction: declined for payment {}", paymentId);
      stateContext.getStateMachine().sendEvent(
          Mono.just(
              MessageBuilder.withPayload(PaymentEventEnum.PRE_AUTH_DECLINED)
                  .setHeader(BasePaymentService.PAYMENT_ID_HEADER, paymentId).build())).subscribe();
    }
  }
}
