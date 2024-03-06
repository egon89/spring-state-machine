package com.egon.statemachine.actions;

import com.egon.statemachine.enums.PaymentEventEnum;
import com.egon.statemachine.enums.PaymentStateEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.action.Action;

@Slf4j
@UtilityClass
public class ActionUtil {
  public static Action<PaymentStateEnum, PaymentEventEnum> functionalAction = stateContext -> {
    log.debug("Functional action called!");
  };
}
