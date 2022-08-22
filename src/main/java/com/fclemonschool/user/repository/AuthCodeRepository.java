package com.fclemonschool.user.repository;

import com.fclemonschool.user.model.entity.AuthCode;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeRepository extends JpaRepository<AuthCode, UUID> {
  AuthCode getAuthCodeByPhone(String phone);
}
