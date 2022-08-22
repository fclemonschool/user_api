package com.fclemonschool.user.controller;

import com.fclemonschool.user.model.request.AuthCodeRequest;
import com.fclemonschool.user.model.response.AuthCodeResponse;
import com.fclemonschool.user.model.response.UserLoginResponse;
import com.fclemonschool.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 Controller.
 */
@Tag(name = "Auths", description = "인증 API")
@RestController
@RequestMapping("/api/v1/auths")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  /**
   * 로그인(id, 이메일, 전화 번호)
   *
   * @param loginInfo 로그인 정보
   * @return 사용자 로그인 Vo의 HTTP 형식 응답 객체
   */
  @Operation(summary = "로그인", description = "로그인 기능.")
  @PostMapping
  public ResponseEntity<UserLoginResponse> login(@RequestBody Map<String, String> loginInfo) {
    return ResponseEntity.ok(authService.login(loginInfo));
  }

  /**
   * 인증 번호 전송
   *
   * @param request 인증 번호 Dto
   * @return void
   */
  @Operation(summary = "인증 번호 전송", description = "인증 번호 전송 기능.")
  @PostMapping("/codes")
  public ResponseEntity<Object> sendAuthCode(@RequestBody AuthCodeRequest request) {
    authService.sendAuthCode(request.getPhone());
    return ResponseEntity.ok().build();
  }

  /**
   * 인증 번호 조회(휴대폰 수신에 대한 대응)
   *
   * @param phone 전화 번호
   * @return 인증 번호 Vo의 HTTP 형식 응답 객체
   */
  @Operation(summary = "인증 번호 조회", description = "인증 번호 조회(휴대폰 수신에 대한 대응) 기능.")
  @GetMapping("/codes/{phone}")
  public ResponseEntity<AuthCodeResponse> retrieveAuthCode(@PathVariable String phone) {
    return ResponseEntity.ok(authService.retrieveAuthCode(phone));
  }

  /**
   * 인증 번호 인증 처리
   *
   * @param request 인증 번호 Dto
   * @return 인증 번호 Vo의 HTTP 형식 응답 객체
   */
  @Operation(summary = "인증 번호 인증 처리", description = "인증 번호 인증 처리 기능.")
  @PostMapping("/codes/{phone}")
  public ResponseEntity<AuthCodeResponse> checkAuthCode(@RequestBody AuthCodeRequest request) {
    return ResponseEntity.ok(authService.checkAuthCode(request.getPhone(), request.getCode()));
  }
}
