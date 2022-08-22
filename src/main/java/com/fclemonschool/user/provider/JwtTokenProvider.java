package com.fclemonschool.user.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  @Value("${secret-key}")
  private String secretKey;

  @Value("#{new Long(${token-valid-time})}")
  private Long tokenValidTime;

  private final UserDetailsService userDetailsService;

  public JwtTokenProvider(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes()); // secretKey를 Base64로 인코딩.
  }

  public String createToken(String uuidString, String email) {
    Claims claims = Jwts.claims().setSubject(uuidString); // JWT payload에 저장되는 정보단위
    claims.put("email", email); // key / value
    Date now = new Date();
    return Jwts.builder().setClaims(claims) // 정보 저장
        .setIssuedAt(now) // 토큰 발행 시간
        .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))  // secretKey 세팅
        .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(getUuidStringFromToken(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUuidStringFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  public String getToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring("Bearer ".length());
    }
    return null;
  }

  public boolean validate(String jwtToken) {
    try {
      Jws<Claims> claims =
          Jwts.parserBuilder().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).build()
              .parseClaimsJws(jwtToken);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }
}
