package com.egon.statemachine.mappers;

import com.egon.statemachine.dtos.PaymentDto;
import com.egon.statemachine.entities.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentMapper {
  PaymentEntity toEntity(PaymentDto dto);
  PaymentDto toDto(PaymentEntity entity);
}
