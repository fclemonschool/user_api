package com.fclemonschool.user.model.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 비밀번호 Dto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordRequest {
  @NotNull
  private String password;
  @NotNull
  private String phone;
}
