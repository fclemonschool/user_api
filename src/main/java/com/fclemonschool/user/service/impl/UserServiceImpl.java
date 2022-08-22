package com.fclemonschool.user.service.impl;

import static com.fclemonschool.user.exception.ExceptionType.ALREADY_USED_AUTH_CODE_EXCEPTION;
import static com.fclemonschool.user.exception.ExceptionType.NOT_AUTHORIZED_PHONE_EXCEPTION;
import static com.fclemonschool.user.exception.ExceptionType.VALIDATION_EXCEPTION;
import static com.fclemonschool.user.mapper.UserMapper.INSTANCE;

import com.fclemonschool.user.exception.CustomException;
import com.fclemonschool.user.model.entity.User;
import com.fclemonschool.user.model.request.UserPasswordRequest;
import com.fclemonschool.user.model.request.UserRequest;
import com.fclemonschool.user.model.response.AuthCodeResponse;
import com.fclemonschool.user.model.response.UserPasswordResponse;
import com.fclemonschool.user.model.response.UserResponse;
import com.fclemonschool.user.repository.UserRepository;
import com.fclemonschool.user.service.AuthService;
import com.fclemonschool.user.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final AuthService authService;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, AuthService authService,
                         PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.authService = authService;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  @Override
  public UserResponse create(UserRequest request) {
    AuthCodeResponse authCodeResponse = authService.retrieveAuthCode(request.getPhone());
    if (authCodeResponse != null && authCodeResponse.isChecked() && !authCodeResponse.isUsed()) {
      request.setPassword(passwordEncoder.encode(request.getPassword()));
      authService.useAuthCode(request.getPhone(), authCodeResponse.getCode());
      return INSTANCE.toVo(userRepository.save(INSTANCE.toEntity(request)));
    }
    if (authCodeResponse == null || !authCodeResponse.isChecked()) {
      throw new CustomException(NOT_AUTHORIZED_PHONE_EXCEPTION);
    }
    if (authCodeResponse.isUsed()) {
      throw new CustomException(ALREADY_USED_AUTH_CODE_EXCEPTION);
    }
    throw new CustomException(VALIDATION_EXCEPTION);
  }

  @Override
  public UserResponse retrieveMyInfo() {
    return INSTANCE.toVo(
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  @Transactional
  @Override
  public UserPasswordResponse resetPassword(UserPasswordRequest request) {
    AuthCodeResponse authCodeResponse = authService.retrieveAuthCode(request.getPhone());
    if (authCodeResponse != null && authCodeResponse.isChecked() && !authCodeResponse.isUsed()) {
      User user = userRepository.getUserByPhone(request.getPhone());
      user.setPassword(passwordEncoder.encode(request.getPassword()));
      if (passwordEncoder.matches(request.getPassword(), userRepository.save(user).getPassword())) {
        authService.useAuthCode(request.getPhone(), authCodeResponse.getCode());
        return UserPasswordResponse.builder().phone(request.getPhone()).changed(true).build();
      }
    }
    if (authCodeResponse == null || !authCodeResponse.isChecked()) {
      throw new CustomException(NOT_AUTHORIZED_PHONE_EXCEPTION);
    }
    if (authCodeResponse.isUsed()) {
      throw new CustomException(ALREADY_USED_AUTH_CODE_EXCEPTION);
    }
    throw new CustomException(VALIDATION_EXCEPTION);
  }
}
