package com.fclemonschool.user.service.impl;

import static com.fclemonschool.user.exception.ExceptionType.NOT_VALID_LOGIN_INFO_EXCEPTION;
import static com.fclemonschool.user.mapper.AuthCodeMapper.INSTANCE;

import com.fclemonschool.user.exception.CustomException;
import com.fclemonschool.user.provider.JwtTokenProvider;
import com.fclemonschool.user.model.entity.AuthCode;
import com.fclemonschool.user.model.entity.User;
import com.fclemonschool.user.model.request.AuthCodeRequest;
import com.fclemonschool.user.model.response.AuthCodeResponse;
import com.fclemonschool.user.model.response.UserLoginResponse;
import com.fclemonschool.user.repository.AuthCodeRepository;
import com.fclemonschool.user.repository.UserRepository;
import com.fclemonschool.user.service.AuthService;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final AuthCodeRepository authCodeRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final Random rand;

  public AuthServiceImpl(UserRepository userRepository, AuthCodeRepository authCodeRepository,
                         JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder)
      throws NoSuchAlgorithmException {
    this.userRepository = userRepository;
    this.authCodeRepository = authCodeRepository;
    this.jwtTokenProvider = jwtTokenProvider;
    this.passwordEncoder = passwordEncoder;
    this.rand = SecureRandom.getInstanceStrong();
  }

  @Override
  public UserLoginResponse login(Map<String, String> loginInfo) {
    String phone = loginInfo.get("phone");
    String email = loginInfo.get("email");
    String id = loginInfo.get("id");
    String password = loginInfo.get("password");
    User user;
    if (phone != null) {
      user = userRepository.getUserByPhone(phone);
    } else if (email != null) {
      user = userRepository.getUserByEmail(email);
    } else if (id != null) {
      user = userRepository.getReferenceById(UUID.fromString(id));
    } else {
      throw new CustomException(NOT_VALID_LOGIN_INFO_EXCEPTION);
    }
    return isPasswordMatched(password, user);
  }

  private UserLoginResponse isPasswordMatched(String password, User user) {
    if (passwordEncoder.matches(password, userRepository.getUserPasswordByEmail(user.getEmail()))) {
      return UserLoginResponse.builder()
          .token(jwtTokenProvider.createToken(user.getId().toString(), user.getEmail())).build();
    }
    throw new CustomException(NOT_VALID_LOGIN_INFO_EXCEPTION);
  }

  @Transactional
  @Override
  public void sendAuthCode(String phone) {
    String code = String.format("%06d", rand.nextInt(999999));
    AuthCodeRequest request = AuthCodeRequest.builder().code(code).phone(phone)
        .expiryTime(LocalDateTime.now().plusMinutes(3)).build();
    authCodeRepository.save(INSTANCE.toEntity(request));
  }

  @Override
  public AuthCodeResponse retrieveAuthCode(String phone) {
    return INSTANCE.toVo(authCodeRepository.getAuthCodeByPhone(phone));
  }

  @Transactional
  @Override
  public AuthCodeResponse checkAuthCode(String phone, String code) {
    AuthCode authCode = authCodeRepository.getAuthCodeByPhone(phone);
    if (!authCode.isChecked() && authCode.getCode().equals(code) &&
        authCode.getExpiryTime().isAfter(LocalDateTime.now())) {
      authCode.setChecked(true);
      return INSTANCE.toVo(authCodeRepository.save(authCode));
    }
    return INSTANCE.toVo(authCode);
  }

  @Override
  public void useAuthCode(String phone, String code) {
    AuthCode authCode = authCodeRepository.getAuthCodeByPhone(phone);
    if (authCode.isChecked() && authCode.getCode().equals(code)) {
      authCode.setUsed(true);
      authCodeRepository.save(authCode);
    }
  }
}
