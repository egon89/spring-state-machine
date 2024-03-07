package com.egon.statemachine;

import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import com.egon.statemachine.services.BasePaymentService;
import lombok.experimental.UtilityClass;
import org.springframework.statemachine.StateContext;

import java.util.Objects;

@UtilityClass
public class StateContextHelper {

  public static Long getPaymentId(StateContext<?, ?> stateContext) {
    final var value = stateContext.getMessageHeader(BasePaymentService.PAYMENT_ID_HEADER);
    Objects.requireNonNull(value, "Payment id header not found");
    return (Long) value;
  }
}
