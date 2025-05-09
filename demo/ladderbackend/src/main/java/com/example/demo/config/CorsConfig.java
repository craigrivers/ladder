package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig {
    private static final Logger logger = LoggerFactory.getLogger(CorsConfig.class);
    
    private static final List<String> DEFAULT_ALLOWED_ORIGINS = List.of(
        "http://localhost",
        "http://localhost:80",
        "http://localhost:4200",
        "https://localhost:8081",
        "https://ladder-frontend-vj79.onrender.com",
        "http://ladder-frontend-vj79.onrender.com",
        "http://ladder-app-a4ra.onrender.com",
        "https://dmvtennisladders.com"  
    );

    private static final List<String> DEFAULT_ALLOWED_METHODS = Arrays.stream(HttpMethod.values())
        .map(HttpMethod::name)
        .collect(Collectors.toList());

    private static final List<String> DEFAULT_ALLOWED_HEADERS = List.of(
        "Authorization",
        "Content-Type",
        "X-Requested-With",
        "Accept",
        "Origin",
        "Access-Control-Request-Method",
        "Access-Control-Request-Headers",
        "X-CSRF-TOKEN",
        "Access-Control-Allow-Origin",
        "Access-Control-Allow-Methods",
        "Access-Control-Allow-Headers",
        "Access-Control-Allow-Credentials",
        "Access-Control-Max-Age",
        "X-Forwarded-Proto",
        "X-Forwarded-For",
        "X-Forwarded-Host"
    );

    private static final List<String> EXPOSED_HEADERS = List.of(
        "Access-Control-Allow-Origin",
        "Access-Control-Allow-Credentials",
        "Access-Control-Allow-Methods",
        "Access-Control-Allow-Headers",
        "X-CSRF-TOKEN"
    );

    @Value("${spring.mvc.cors.allowed-origins:#{null}}")
    private String allowedOrigins;

    @Value("${spring.mvc.cors.allowed-methods:#{null}}")
    private String allowedMethods;

    @Value("${spring.mvc.cors.allowed-headers:#{null}}")
    private String allowedHeaders;

    @Value("${spring.mvc.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${spring.mvc.cors.max-age:3600}")
    private long maxAge;

    @Bean
    public CorsFilter corsFilter() {
        logger.info("Initializing CORS filter with allowed origins: {}", allowedOrigins);
        
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // Convert String values to Lists
        List<String> origins = allowedOrigins != null ? 
            Arrays.asList(allowedOrigins.split(",")) : 
            DEFAULT_ALLOWED_ORIGINS;
            
        List<String> methods = allowedMethods != null ? 
            Arrays.asList(allowedMethods.split(",")) : 
            DEFAULT_ALLOWED_METHODS;
            
        List<String> headers = allowedHeaders != null ? 
            Arrays.asList(allowedHeaders.split(",")) : 
            DEFAULT_ALLOWED_HEADERS;

        corsConfiguration.setAllowedOriginPatterns(origins);
        corsConfiguration.setAllowedMethods(methods);
        corsConfiguration.setAllowedHeaders(headers);
        corsConfiguration.setExposedHeaders(EXPOSED_HEADERS);
        corsConfiguration.setAllowCredentials(allowCredentials);
        corsConfiguration.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        
        logger.info("CORS filter initialized with configuration: {}", corsConfiguration);
        return new CorsFilter(source);
    }

    private void logCorsConfiguration(List<String> origins, List<String> methods, List<String> headers) {
        logger.info("Configuring CORS with the following settings:");
        logger.info("Allowed origins: {}", String.join(", ", origins));
        logger.info("Allowed methods: {}", String.join(", ", methods));
        logger.info("Allowed headers: {}", String.join(", ", headers));
        logger.info("Allow credentials: {}", allowCredentials);
        logger.info("Max age: {} seconds", maxAge);
    }
} 