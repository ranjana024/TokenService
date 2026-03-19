package com.demo.config;

import com.demo.filter.ApiKeyAuthFilter;
import com.demo.filter.JwtAuthFilter;
import com.demo.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter (JwtUtil jwtUtil) {
        return new JwtAuthFilter(jwtUtil);
    }

    @Bean
    public ApiKeyAuthFilter apiKeyAuthFilter() {
        return new ApiKeyAuthFilter();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           ApiKeyAuthFilter apiKeyAuthFilter,
                                           JwtAuthFilter jwtAuthFilter) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/actuator/**"
                        ).permitAll()

                        // API Key protected endpoint
                        .requestMatchers("/api/auth/token").authenticated()

                        // JWT protected endpoints
                        .requestMatchers(
                                "/api/v1/tokenization/tokenize",
                                "/api/v1/tokenization/detokenize"
                        ).authenticated()

                        .anyRequest().permitAll()
                )

                // Order matters: API Key first, then JWT
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, ApiKeyAuthFilter.class);

        return http.build();
    }
}