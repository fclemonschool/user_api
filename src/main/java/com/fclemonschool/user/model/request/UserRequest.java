package com.fclemonschool.user.model.request;

import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 Dto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
  private UUID id;
  @Email
  private String email;
  @NotNull
  private String nickname;
  @NotNull
  private String password;
  @NotNull
  private String username;
  @NotNull
  private String phone;
}
