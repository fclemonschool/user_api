package com.fclemonschool.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fclemonschool.user.mapper.AuthCodeMapper;
import com.fclemonschool.user.mapper.UserMapper;
import com.fclemonschool.user.model.entity.AuthCode;
import com.fclemonschool.user.model.entity.User;
import com.fclemonschool.user.model.request.AuthCodeRequest;
import com.fclemonschool.user.model.request.UserRequest;
import com.fclemonschool.user.model.response.AuthCodeResponse;
import com.fclemonschool.user.model.response.UserLoginResponse;
import com.fclemonschool.user.provider.JwtTokenProvider;
import com.fclemonschool.user.repository.AuthCodeRepository;
import com.fclemonschool.user.repository.UserRepository;
import com.fclemonschool.user.service.impl.AuthServiceImpl;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
  AuthService authService;
  @Mock
  AuthCodeRepository authCodeRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  JwtTokenProvider jwtTokenProvider;
  @Spy
  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  @Spy
  AuthCodeMapper authMapper = Mappers.getMapper(AuthCodeMapper.class);
  @Spy
  UserMapper userMapper = Mappers.getMapper(UserMapper.class);
  String phone;
  String password;
  String encodedPassword;
  String email;
  String uuidString;
  String token;

  @BeforeEach
  public void initAll() throws NoSuchAlgorithmException {
    this.authService =
        new AuthServiceImpl(userRepository, authCodeRepository, jwtTokenProvider, passwordEncoder);
    phone = "01012345673";
    password = "1q2w3e4r";
    encodedPassword = passwordEncoder.encode(password);
    email = "abc@yopmail.com";
    uuidString = "2cfb432e-be6e-4610-9e2d-c22dfe30eb48";
    token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4OTI1NGMwMi00NmI3LTQ3ZDAtODg4Ni03NTUwZTA0ZTY2NzMiLCJ" +
        "lbWFpbCI6ImFiY0B5b3BtYWlsLmNvbSIsImlhdCI6MTY2MTE3MjM1NCwiZXhwIjoxNjYxMTc0MTU0fQ.g4Dr4xta" +
        "N1EX2AjULskg8Fy5FEswaKBa_XHNffSA8zU";
  }

  @Test
  void login() {
    // given
    Map<String, String> loginInfo = new HashMap<>();
    loginInfo.put("phone", phone);
    loginInfo.put("password", password);

    UserRequest request =
        UserRequest.builder().id(UUID.fromString(uuidString)).username("abc").nickname("def")
            .password(encodedPassword).email(email).phone(phone).build();
    User entity = userMapper.toEntity(request);
    when(userRepository.getUserPasswordByEmail(any())).thenReturn(encodedPassword);
    when(userRepository.getUserByPhone(any())).thenReturn(entity);
    when(jwtTokenProvider.createToken(any(), any())).thenReturn(token);

    // when
    UserLoginResponse result = authService.login(loginInfo);

    // then
    verify(userRepository).getUserPasswordByEmail(email);
    verify(userRepository).getUserByPhone(phone);
    verify(jwtTokenProvider).createToken(uuidString, email);
    assertEquals(token, result.getToken());
  }

  @Test
  void sendAuthCode() {
    // given
    AuthCodeRequest request = AuthCodeRequest.builder().code("012345").phone(phone)
        .expiryTime(LocalDateTime.now().plusMinutes(3)).build();
    AuthCode entity = authMapper.toEntity(request);
    when(authCodeRepository.save(any())).thenReturn(entity);

    // when
    authService.sendAuthCode(phone);

    // then
    verify(authCodeRepository).save(entity);
  }

  @Test
  void retrieveAuthCode() {
    // given
    AuthCodeRequest request = AuthCodeRequest.builder().code("012345").phone(phone)
        .expiryTime(LocalDateTime.now().plusMinutes(3)).build();
    AuthCode entity = authMapper.toEntity(request);
    when(authCodeRepository.getAuthCodeByPhone(any())).thenReturn(entity);

    // when
    AuthCodeResponse result = authService.retrieveAuthCode(phone);

    // then
    verify(authCodeRepository).getAuthCodeByPhone(phone);
    assertEquals(result, authMapper.toVo(entity));
  }

  @Test
  void checkAuthCode() {
    // given
    AuthCodeRequest request = AuthCodeRequest.builder().code("012345").phone(phone)
        .expiryTime(LocalDateTime.now().plusMinutes(3)).build();
    AuthCode entity = authMapper.toEntity(request);
    when(authCodeRepository.getAuthCodeByPhone(any())).thenReturn(entity);
    when(authCodeRepository.save(any())).thenReturn(entity);

    // when
    authService.checkAuthCode(phone, "012345");

    // then
    verify(authCodeRepository).getAuthCodeByPhone(phone);
    verify(authCodeRepository).save(entity);
  }

  @Test
  void useAuthCode() {
    // given
    AuthCodeRequest request = AuthCodeRequest.builder().code("012345").phone(phone)
        .expiryTime(LocalDateTime.now().plusMinutes(3)).build();
    AuthCode entity = authMapper.toEntity(request);
    entity.setChecked(true);
    when(authCodeRepository.getAuthCodeByPhone(any())).thenReturn(entity);
    when(authCodeRepository.save(any())).thenReturn(entity);

    // when
    authService.useAuthCode(phone, "012345");

    // then
    verify(authCodeRepository).getAuthCodeByPhone(phone);
    verify(authCodeRepository).save(entity);
  }
}
