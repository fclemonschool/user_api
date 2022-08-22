package com.fclemonschool.user.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fclemonschool.user.mapper.AuthCodeMapper;
import com.fclemonschool.user.model.request.AuthCodeRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthCodeRepositoryTest {
  @Autowired
  AuthCodeRepository authCodeRepository;

  @Spy
  AuthCodeMapper mapper = Mappers.getMapper(AuthCodeMapper.class);

  @Test
  void getAuthCodeByPhone() {
    // given
    String phone = "01012345678";
    authCodeRepository.save(
        mapper.toEntity(AuthCodeRequest.builder().phone(phone).code("012345").expiryTime(
            LocalDateTime.now().plusMinutes(3)).build()));

    // then
    assertNull(authCodeRepository.getAuthCodeByPhone("01109877890"));
    assertNotNull(authCodeRepository.getAuthCodeByPhone(phone));
  }
}
