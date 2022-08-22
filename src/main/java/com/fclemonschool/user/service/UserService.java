package com.fclemonschool.user.service;

import com.fclemonschool.user.model.request.UserPasswordRequest;
import com.fclemonschool.user.model.request.UserRequest;
import com.fclemonschool.user.model.response.UserPasswordResponse;
import com.fclemonschool.user.model.response.UserResponse;

public interface UserService {
  UserResponse create(UserRequest request);

  UserResponse retrieveMyInfo();

  UserPasswordResponse resetPassword(UserPasswordRequest request);
}
