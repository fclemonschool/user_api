package com.fclemonschool.user.repository;

import com.fclemonschool.user.model.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {
  User getUserByEmail(String email);

  User getUserByPhone(String phone);

  Optional<User> findOptionalById(UUID id);

  @Query("select u.password from User u where u.email = ?1")
  String getUserPasswordByEmail(String email);
}
