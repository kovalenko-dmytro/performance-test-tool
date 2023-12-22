package com.gmail.apachdima.ptt.notification.email.mapper;

import com.gmail.apachdima.ptt.common.dto.email.EmailResponseDTO;
import com.gmail.apachdima.ptt.notification.email.model.EmailLog;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EmailMapper {

    EmailResponseDTO toEmailResponseDTO(EmailLog emailLog);
}
