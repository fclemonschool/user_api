package com.fclemonschool.user.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 인증 번호 Entity.
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AuthCode {
  @Id
  @Column(nullable = false, unique = true)
  private String phone;
  @Column(nullable = false)
  private String code;
  @Column(nullable = false)
  private LocalDateTime expiryTime;
  @Column(nullable = false)
  private boolean checked;
  @Column(nullable = false)
  private boolean used;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    AuthCode authCode = (AuthCode) o;
    return phone != null && Objects.equals(phone, authCode.phone);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
