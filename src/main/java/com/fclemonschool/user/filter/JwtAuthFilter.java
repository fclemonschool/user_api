package com.fclemonschool.user.filter;

import com.fclemonschool.user.provider.JwtTokenProvider;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String token = jwtTokenProvider.getToken((HttpServletRequest) request);
    if (token != null && jwtTokenProvider.validate(token)) {
      SecurityContextHolder.getContext()
          .setAuthentication(jwtTokenProvider.getAuthentication(token));
    }
    chain.doFilter(request, response);
  }
}
