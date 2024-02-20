package com.egon.statemachine.dtos;

import com.egon.statemachine.enums.PaymentStateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
  private Long id;
  private PaymentStateEnum state;
  private BigDecimal amount;
}
