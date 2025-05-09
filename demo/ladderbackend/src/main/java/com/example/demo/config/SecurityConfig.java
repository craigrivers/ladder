package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .cors().and()
      .csrf().disable()
      .authorizeHttpRequests()
        .anyRequest().permitAll();
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(List.of(
      "https://ladder-frontend-vj79.onrender.com",
      "https://ladder-app-a4ra.onrender.com",
      "http://localhost:4200",
      "http://localhost",
      "http://localhost:80",
      "https://localhost:8081",
      "http://ladder-app-a4ra.onrender.com",
      "http://ladder-app-internal:8081" 
    ));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
    configuration.setAllowedHeaders(List.of(
      "Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin",
      "Access-Control-Request-Method", "Access-Control-Request-Headers",
      "X-CSRF-TOKEN", "Access-Control-Allow-Origin", "Access-Control-Allow-Methods",
      "Access-Control-Allow-Headers", "Access-Control-Allow-Credentials",
      "Access-Control-Max-Age", "X-Forwarded-Proto", "X-Forwarded-For", "X-Forwarded-Host"
    ));
    configuration.setExposedHeaders(List.of(
      "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials",
      "Access-Control-Allow-Methods", "Access-Control-Allow-Headers", "X-CSRF-TOKEN"
    ));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
