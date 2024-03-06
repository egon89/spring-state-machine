package com.egon.statemachine.integrations;

public interface CheckPreAuthorizeCreditIntegration {
  boolean execute(Long id);
}
