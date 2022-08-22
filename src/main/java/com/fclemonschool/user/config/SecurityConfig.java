package com.fclemonschool.user.config;

import com.fclemonschool.user.filter.JwtAuthFilter;
import com.fclemonschool.user.provider.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 관련 설정.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;

  public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
          .antMatchers("/api/v1/auths/**").permitAll()
          .antMatchers("/api/v1/users", "/api/v1/users/**/password").permitAll()
          .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
          .antMatchers("/**").authenticated()
        .and()
          .addFilterBefore(new JwtAuthFilter(jwtTokenProvider),
              UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
