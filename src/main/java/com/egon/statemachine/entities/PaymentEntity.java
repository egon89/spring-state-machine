package com.egon.statemachine.entities;

import com.egon.statemachine.enums.PaymentStateEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_payment")
@Entity
public class PaymentEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  private PaymentStateEnum state;

  private BigDecimal amount;
}
