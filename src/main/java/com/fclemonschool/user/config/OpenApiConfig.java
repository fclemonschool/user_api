package com.fclemonschool.user.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger Open Api 설정.
 */
@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig {

  /**
   * Swagger UI에 표시되는 타이틀, 버전 및 설명 설정.
   *
   * @return OpenAPI
   */
  @Bean
  public OpenAPI springOpenApi() {
    return new OpenAPI().info(new Info().title("User API")
        .description("사용자 API 명세서입니다.").version("v0.0.1"));
  }
}
