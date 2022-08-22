package com.fclemonschool.user.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fclemonschool.user.mapper.UserMapper;
import com.fclemonschool.user.model.entity.User;
import com.fclemonschool.user.model.request.UserRequest;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
  @Autowired
  UserRepository userRepository;

  @Spy
  UserMapper mapper = Mappers.getMapper(UserMapper.class);

  private UUID id;

  @BeforeEach
  public void initAll() {
    User user = userRepository.save(mapper.toEntity(
        UserRequest.builder().username("abc").nickname("def").password("1q2w3e4r")
            .email("abc@yopmail.com").phone("01012345678").build()));
    id = user.getId();
  }

  @Test
  void getUserByEmail() {
    // given
    String target = "abc@yopmail.com";

    // when
    User result = userRepository.getUserByEmail(target);

    // then
    assertNotNull(result);
  }

  @Test
  void getUserByPhone() {
    // given
    String target = "01012345678";

    // when
    User result = userRepository.getUserByPhone(target);

    // then
    assertNotNull(result);
  }

  @Test
  void findOptionalById() {
    // when
    Optional<User> result = userRepository.findOptionalById(id);

    // then
    assertTrue(result.isPresent());
    assertNotNull(result.get());
  }

  @Test
  void getUserPasswordByEmail() {
    // given
    String target = "abc@yopmail.com";

    // when
    User result = userRepository.getUserByEmail(target);

    // then
    assertNotNull(result);
  }
}
