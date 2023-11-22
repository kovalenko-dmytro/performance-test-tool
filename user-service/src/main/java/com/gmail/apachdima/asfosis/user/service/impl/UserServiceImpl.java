package com.gmail.apachdima.asfosis.user.service.impl;

import com.gmail.apachdima.asfosis.common.constant.message.Error;
import com.gmail.apachdima.asfosis.common.constant.model.Model;
import com.gmail.apachdima.asfosis.common.dto.auth.SignUpRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.user.UpdateUserRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.user.UserCreateRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.user.UserResponseDTO;
import com.gmail.apachdima.asfosis.common.exception.EntityNotFoundException;
import com.gmail.apachdima.asfosis.user.mapper.UserMapper;
import com.gmail.apachdima.asfosis.user.model.User;
import com.gmail.apachdima.asfosis.user.model.UserRole;
import com.gmail.apachdima.asfosis.user.repository.RoleRepository;
import com.gmail.apachdima.asfosis.user.repository.UserRepository;
import com.gmail.apachdima.asfosis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).stream()
            .map(userMapper::toUserResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO findById(String userId, Locale locale) {
        return userMapper.toUserResponseDTO(getById(userId, locale));
    }

    @Override
    public UserResponseDTO create(UserCreateRequestDTO userRequestDTO) {
        User user = User.builder()
            .username(userRequestDTO.getUserName())
            .password(passwordEncoder.encode(userRequestDTO.getPassword().trim()))
            .firstName(userRequestDTO.getFirstName().trim())
            .lastName(userRequestDTO.getLastName().trim())
            .email(userRequestDTO.getEmail().trim())
            .enabled(true)
            .created(LocalDateTime.now())
            .roles(Set.of(roleRepository.findByRole(UserRole.USER).get()))
            .build();
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO update(String userId, UpdateUserRequestDTO request, Locale locale) {
        User user = getById(userId, locale);
        if (StringUtils.isNoneBlank(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }
        if (StringUtils.isNoneBlank(request.getLastName())) {
            user.setLastName(request.getLastName());
        }
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    @Override
    public void delete(String userId, Locale locale) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User loggedInUser = getByUsername(authentication.getName(), locale);
        User deletableUser = getById(userId, locale);
        Set<String> loggedInUserRoles = loggedInUser.getRoles().stream()
            .map(role -> role.getRole().name())
            .collect(Collectors.toSet());
        Set<String> deletableUserRoles = deletableUser.getRoles().stream()
            .map(role -> role.getRole().name())
            .collect(Collectors.toSet());
        if (loggedInUserRoles.contains(UserRole.ADMIN.name())
            || (loggedInUserRoles.contains(UserRole.MANAGER.name()) && deletableUserRoles.contains(UserRole.USER.name()))) {
            userRepository.deleteById(userId);
        }
    }

    @Override
    public User getByUsername(String userName, Locale locale) {
        Object[] params = new Object[]{Model.USER.getName(), Model.Field.USER_NAME.getFieldName(), userName};
        return userRepository
            .findByUsername(userName)
            .orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage(Error.ENTITY_NOT_FOUND.getKey(), params, locale)));
    }

    private User getById(String userId, Locale locale) {
        Object[] params = new Object[]{Model.USER.getName(), Model.Field.ID.getFieldName(), userId};
        return userRepository
            .findById(userId)
            .orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage(Error.ENTITY_NOT_FOUND.getKey(), params, locale)));
    }
}
