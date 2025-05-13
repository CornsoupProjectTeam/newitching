package com.cornsoup.newitching.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 패스워드 암호화를 위한 Bean 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (API 서버인 경우 보통 끔)
                .authorizeHttpRequests(auth -> auth
                        // 매칭 아이디 중복확인, 매칭 등록, 멤버 등록, 팀매칭 결과 조회는 인증 없이 허용
                        .requestMatchers(
                                "/matching/doublecheck",
                                "/matching/register",
                                "/{urlKey}",
                                "/{urlKey}/register",
                                "/matching/{matchingId}"
                        ).permitAll()
                        // 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // React에서 오는 password 값 복호화를 위한 Bean 등록
    @Bean
    public PasswordDecryptor passwordDecryptor() {
        return new FromReactPasswordDecryptor(); // 직접 new 해서 등록
    }

    @Configuration
    public class CorsConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    //.allowedOrigins("10.0.0.18:3000") // 프론트 주소
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    //.allowCredentials(true);
                    .allowCredentials(false);
        }
    }

}
