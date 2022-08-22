package com.fclemonschool.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fclemonschool.user.model.request.AuthCodeRequest;
import com.fclemonschool.user.service.AuthService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
class AuthControllerTest {
  @MockBean
  AuthService authService;
  private ObjectMapper objectMapper;
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @BeforeEach
  public void initAll() {
    this.objectMapper = new ObjectMapper();
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void loginWithPhone() throws Exception {
    // given
    Map<String, String> phone = new HashMap<>();
    phone.put("phone", "01012345678");
    phone.put("password", "1q2w3e4r");

    Map<String, String> id = new HashMap<>();
    id.put("id", "2cfb432e-be6e-4610-9e2d-c22dfe30eb48");
    id.put("password", "1q2w3e4r");

    Map<String, String> email = new HashMap<>();
    email.put("phone", "abc@yopmail.com");
    email.put("password", "1q2w3e4r");

    // then
    mockMvc.perform(post("/api/v1/auths").content(objectMapper.writeValueAsString(phone))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    mockMvc.perform(post("/api/v1/auths").content(objectMapper.writeValueAsString(id))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    mockMvc.perform(post("/api/v1/auths").content(objectMapper.writeValueAsString(email))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
  }

  @Test
  void sendAuthCode() throws Exception {
    // given
    AuthCodeRequest request = AuthCodeRequest.builder().phone("01011112222").build();

    // then
    mockMvc.perform(post("/api/v1/auths/codes").content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
  }

  @Test
  void retrieveAuthCode() throws Exception {
    // given
    String phone = "01011112222";

    // then
    mockMvc.perform(get("/api/v1/auths/codes/" + phone)
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
  }

  @Test
  void checkAuthCode() throws Exception {
    // given
    String phone = "01011112222";
    AuthCodeRequest request = AuthCodeRequest.builder().code("012345").build();

    // then
    mockMvc.perform(
        post("/api/v1/auths/codes/" + phone).content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
  }
}
