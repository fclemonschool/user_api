package com.fclemonschool.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fclemonschool.user.mapper.AuthCodeMapper;
import com.fclemonschool.user.mapper.UserMapper;
import com.fclemonschool.user.model.entity.AuthCode;
import com.fclemonschool.user.model.entity.User;
import com.fclemonschool.user.model.request.AuthCodeRequest;
import com.fclemonschool.user.model.request.UserPasswordRequest;
import com.fclemonschool.user.model.request.UserRequest;
import com.fclemonschool.user.model.response.UserPasswordResponse;
import com.fclemonschool.user.model.response.UserResponse;
import com.fclemonschool.user.provider.JwtTokenProvider;
import com.fclemonschool.user.repository.AuthCodeRepository;
import com.fclemonschool.user.repository.UserRepository;
import com.fclemonschool.user.service.impl.AuthServiceImpl;
import com.fclemonschool.user.service.impl.UserServiceImpl;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  UserService userService;
  AuthService authService;
  @Mock
  UserRepository userRepository;
  @Mock
  AuthCodeRepository authCodeRepository;
  @Mock
  JwtTokenProvider jwtTokenProvider;
  @Spy
  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  @Spy
  UserMapper userMapper = Mappers.getMapper(UserMapper.class);
  @Spy
  AuthCodeMapper authMapper = Mappers.getMapper(AuthCodeMapper.class);
  String phone;
  String password;
  String encodedPassword;
  String email;
  String uuidString;
  UserRequest request;

  @BeforeEach
  public void initAll() throws NoSuchAlgorithmException {
    this.authService =
        new AuthServiceImpl(userRepository, authCodeRepository, jwtTokenProvider, passwordEncoder);
    this.userService = new UserServiceImpl(userRepository, authService, passwordEncoder);
    phone = "01012345673";
    password = "1q2w3e4r";
    encodedPassword = passwordEncoder.encode(password);
    email = "abc@yopmail.com";
    uuidString = "2cfb432e-be6e-4610-9e2d-c22dfe30eb48";
  }

  @Test
  void create() {
    // given
    request =
        UserRequest.builder().phone(phone).email(email).password(encodedPassword).nickname("def")
            .username("abc").build();
    User userEntity = userMapper.toEntity(request);
    AuthCodeRequest codeRequest = AuthCodeRequest.builder().code("012345").phone(phone)
        .expiryTime(LocalDateTime.now().plusMinutes(3)).build();
    AuthCode codeEntity = authMapper.toEntity(codeRequest);
    codeEntity.setChecked(true);
    when(authCodeRepository.getAuthCodeByPhone(any())).thenReturn(codeEntity);
    when(authCodeRepository.save(any())).thenReturn(codeEntity);
    when(userRepository.save(any())).thenReturn(userEntity);

    // when
    UserResponse result = userService.create(request);

    // then
    verify(authCodeRepository, times(2)).getAuthCodeByPhone(phone);
    verify(authCodeRepository).save(codeEntity);
    verify(userRepository).save(any());
    assertEquals(userMapper.toVo(userEntity), result);
  }

  @Test
  void retrieveMyInfo() {
    // given
    request =
        UserRequest.builder().phone(phone).email(email).password(encodedPassword).nickname("def")
            .username("abc").build();
    User userEntity = userMapper.toEntity(request);
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(userEntity, "", userEntity.getAuthorities()));

    // when
    UserResponse result = userService.retrieveMyInfo();

    // then
    assertEquals(userMapper.toVo(userEntity), result);
  }

  @Test
  void resetPassword() {
    // given
    request =
        UserRequest.builder().phone(phone).email(email).password(password).nickname("def")
            .username("abc").build();
    User userEntity = userMapper.toEntity(request);
    userEntity.setPassword(encodedPassword);
    UserPasswordRequest passwordRequest =
        UserPasswordRequest.builder().password(password).phone(phone).build();
    UserPasswordResponse response = UserPasswordResponse.builder().phone(phone).changed(true).build();
    AuthCodeRequest codeRequest = AuthCodeRequest.builder().code("012345").phone(phone)
        .expiryTime(LocalDateTime.now().plusMinutes(3)).build();
    AuthCode codeEntity = authMapper.toEntity(codeRequest);
    codeEntity.setChecked(true);
    when(authCodeRepository.getAuthCodeByPhone(any())).thenReturn(codeEntity);
    when(userRepository.getUserByPhone(any())).thenReturn(userEntity);
    when(userRepository.save(any())).thenReturn(userEntity);

    // when
    UserPasswordResponse result = userService.resetPassword(passwordRequest);

    // then
    verify(authCodeRepository, times(2)).getAuthCodeByPhone(phone);
    verify(userRepository).getUserByPhone(phone);
    verify(userRepository).save(userEntity);
    assertEquals(response, result);
  }
}
