package com.egon.statemachine.guards;

import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import com.egon.statemachine.services.BasePaymentService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.guard.Guard;

import java.util.Objects;

@Slf4j
@UtilityClass
public class GuardUtil {

  public static final Guard<PaymentStateEnum, PaymentEventEnum> PAYMENT_ID_GUARD =
      stateContext -> {
        log.debug("Payment Id Guard checking payment id header");
        return Objects.nonNull(stateContext.getMessageHeader(BasePaymentService.PAYMENT_ID_HEADER));
      };
}
