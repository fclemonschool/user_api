package com.fclemonschool.user.controller;

import com.fclemonschool.user.model.request.UserPasswordRequest;
import com.fclemonschool.user.model.request.UserRequest;
import com.fclemonschool.user.model.response.UserPasswordResponse;
import com.fclemonschool.user.model.response.UserResponse;
import com.fclemonschool.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 Controller.
 */
@Tag(name = "Users", description = "사용자 API")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * 신규 사용자 등록
   *
   * @param request 사용자 Dto
   * @return 사용자 Vo의 HTTP 형식 응답 객체
   */
  @Operation(summary = "신규 사용자 등록", description = "신규 사용자 등록 기능.")
  @PostMapping
  public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
    return ResponseEntity.ok(userService.create(request));
  }

  /**
   * 로그인한 사용자의 정보 조회
   *
   * @return 사용자 Vo의 HTTP 형식 응답 객체
   */
  @Operation(summary = "로그인한 사용자의 정보 조회", description = "로그인한 사용자의 정보 조회 기능.")
  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping("/my")
  public ResponseEntity<UserResponse> retrieveMyInfo() {
    return ResponseEntity.ok(userService.retrieveMyInfo());
  }

  /**
   * 비밀번호 재설정
   *
   * @param phone   전화번호
   * @param request 사용자 비밀번호 Dto
   * @return 사용자 비밀번호 Vo의 HTTP 형식 응답 객체
   */
  @Operation(summary = "비밀번호 재설정", description = "비밀번호 재설정 기능.")
  @PutMapping("/{phone}/password")
  public ResponseEntity<UserPasswordResponse> resetPassword(@PathVariable String phone, @RequestBody
  UserPasswordRequest request) {
    request.setPhone(phone);
    return ResponseEntity.ok(userService.resetPassword(request));
  }
}
