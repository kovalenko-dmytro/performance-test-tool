package com.gmail.apachdima.ptt.user.mapper;

import com.gmail.apachdima.ptt.common.dto.user.UserResponseDTO;
import com.gmail.apachdima.ptt.user.model.Role;
import com.gmail.apachdima.ptt.user.model.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.Objects;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserResponseDTO toUserResponseDTO(User user);

    default String roleToString(Role role) {
        return (Objects.isNull(role)) ? null : role.getRole().name();
    }
}
