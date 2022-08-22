package com.fclemonschool.user.service;

import com.fclemonschool.user.model.response.AuthCodeResponse;
import com.fclemonschool.user.model.response.UserLoginResponse;
import java.util.Map;

public interface AuthService {
  UserLoginResponse login(Map<String, String> loginInfo);

  void sendAuthCode(String phone);

  AuthCodeResponse retrieveAuthCode(String phone);

  AuthCodeResponse checkAuthCode(String phone, String code);

  void useAuthCode(String phone, String code);
}
