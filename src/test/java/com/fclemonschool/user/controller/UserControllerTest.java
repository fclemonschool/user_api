package com.fclemonschool.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fclemonschool.user.model.request.UserPasswordRequest;
import com.fclemonschool.user.model.request.UserRequest;
import com.fclemonschool.user.service.UserService;
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
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
  @MockBean
  UserService userService;
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
  void createUser() throws Exception {
    // given
    UserRequest request =
        UserRequest.builder().phone("01012345670").email("aaa@yopmail.com").password("1q2w3e4r")
            .nickname("abc").username("def").build();
    UserRequest failRequest =
        UserRequest.builder().phone("01012345670").email("aaa").password("1q2w3e4r").nickname("abc")
            .username("def").build();

    // then
    mockMvc.perform(post("/api/v1/users").content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    mockMvc.perform(post("/api/v1/users").content(objectMapper.writeValueAsString(failRequest))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
  }

  @Test
  void retrieveMyInfo() throws Exception {
    // then
    mockMvc.perform(get("/api/v1/users/my")).andExpect(status().isOk());
  }

  @Test
  void resetPassword() throws Exception {
    // given
    String phone = "01012344321";
    UserPasswordRequest request = UserPasswordRequest.builder().password("1q2w3e4r5t").build();

    // then
    mockMvc.perform(put("/api/v1/users/" + phone + "/password").content(
            objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
