package com.gmail.apachdima.ptt.user.service.impl;

import com.gmail.apachdima.ptt.common.constant.message.Error;
import com.gmail.apachdima.ptt.common.constant.model.Model;
import com.gmail.apachdima.ptt.common.dto.user.UpdateUserRequestDTO;
import com.gmail.apachdima.ptt.common.dto.user.UserCreateRequestDTO;
import com.gmail.apachdima.ptt.common.dto.user.UserResponseDTO;
import com.gmail.apachdima.ptt.common.exception.EntityNotFoundException;
import com.gmail.apachdima.ptt.user.mapper.UserMapper;
import com.gmail.apachdima.ptt.user.model.User;
import com.gmail.apachdima.ptt.user.model.UserRole;
import com.gmail.apachdima.ptt.user.repository.RoleRepository;
import com.gmail.apachdima.ptt.user.repository.UserRepository;
import com.gmail.apachdima.ptt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.isEmpty()
            ? Page.empty()
            : users.map(userMapper::toUserResponseDTO);
    }

    @Override
    public UserResponseDTO findById(String userId, Locale locale) {
        return userMapper.toUserResponseDTO(getById(userId, locale));
    }

    @Override
    public UserResponseDTO create(UserCreateRequestDTO userRequestDTO) {
        User user = User.builder()
            .username(userRequestDTO.userName())
            .password(passwordEncoder.encode(userRequestDTO.password().trim()))
            .firstName(userRequestDTO.firstName().trim())
            .lastName(userRequestDTO.lastName().trim())
            .email(userRequestDTO.email().trim())
            .enabled(true)
            .created(LocalDateTime.now())
            .roles(Set.of(roleRepository.findByRole(UserRole.USER).get()))
            .build();
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO update(String userId, UpdateUserRequestDTO request, Locale locale) {
        User user = getById(userId, locale);
        if (StringUtils.isNoneBlank(request.firstName())) {
            user.setFirstName(request.firstName());
        }
        if (StringUtils.isNoneBlank(request.lastName())) {
            user.setLastName(request.lastName());
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
