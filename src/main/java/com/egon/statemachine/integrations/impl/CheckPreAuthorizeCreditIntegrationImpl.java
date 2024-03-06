package com.egon.statemachine.integrations.impl;

import com.egon.statemachine.integrations.CheckPreAuthorizeCreditIntegration;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CheckPreAuthorizeCreditIntegrationImpl implements CheckPreAuthorizeCreditIntegration {
  @Override
  public boolean execute(Long id) {
    // 80% of success and 20% declined
    return new Random().nextInt(10) < 8;
  }
}
