package com.resumeai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화 (REST API이므로)
            .csrf(AbstractHttpConfigurer::disable)

            // CORS 설정 활성화
            .cors(cors -> cors.configurationSource(corsConfigurationSource))

            // 세션 관리 정책 (무상태)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 권한 설정 - 모든 API 허용 (공개 서비스)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/info").permitAll()
                .anyRequest().permitAll()
            );

            // 보안 헤더 설정 (개발환경에서는 단순화)
//            .headers(headers -> headers
//                .frameOptions(frameOptions -> frameOptions.deny())
//                .contentTypeOptions(contentTypeOptions -> contentTypeOptions.and())
//            );

        return http.build();
    }
}