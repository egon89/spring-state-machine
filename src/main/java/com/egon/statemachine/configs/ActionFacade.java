package com.egon.statemachine.configs;

import com.egon.statemachine.actions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ActionFacade {
  final PreAuthorizeCreditAction preAuthorizeCreditAction;
  final PreAuthorizeApprovedAction preAuthorizeApprovedAction;
  final PreAuthorizeDeclinedAction preAuthorizeDeclinedAction;
  final AuthorizationApprovedAction authorizationApprovedAction;
  final AuthorizationDeclinedAction authorizationDeclinedAction;
}
