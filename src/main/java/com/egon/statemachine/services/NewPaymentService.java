package com.egon.statemachine.services;

import com.egon.statemachine.dtos.PaymentDto;

public interface NewPaymentService {
  PaymentDto execute(PaymentDto payment);
}
