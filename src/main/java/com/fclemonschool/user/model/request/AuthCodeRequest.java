package com.fclemonschool.user.model.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 인증 번호 Dto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthCodeRequest {
  @NotNull
  private String phone;
  @NotNull
  private String code;
  private LocalDateTime expiryTime;
}
