package com.fclemonschool.user.model.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 사용자 Entity.
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "USERS")
@RequiredArgsConstructor
public class User implements UserDetails {
  @Id
  @Column(columnDefinition = "uuid")
  @GeneratedValue
  private UUID id;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String nickname;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String username;
  @Column(nullable = false, length = 11, unique = true)
  private String phone;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    User user = (User) o;
    return id != null && Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
