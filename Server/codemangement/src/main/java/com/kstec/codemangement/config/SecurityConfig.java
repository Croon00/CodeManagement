package com.kstec.codemangement.config;

import com.kstec.codemangement.model.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.addAllowedOriginPattern("http://localhost:*"); // 특정 도메인 패턴 허용
                    corsConfiguration.addAllowedMethod("*");
                    corsConfiguration.addAllowedHeader("*");
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/auth/login").permitAll() // Swagger는 인증 불필요
                        .requestMatchers("/api/auth/refresh").permitAll() // RefreshToken 발급은 인증 불필요
                        .requestMatchers("/api/auth/me").authenticated() // /me 경로는 인증 필요
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN") // 유저 생성은 ADMIN만 가능
                        .requestMatchers(HttpMethod.POST, "/api/codes/**").hasAnyRole("ADMIN", "OPERATOR") // 코드 생성은 ADMIN, OPERATOR 가능
                        .requestMatchers(HttpMethod.PUT, "/api/codes/**").hasAnyRole("ADMIN", "OPERATOR") // 코드 수정은 ADMIN, OPERATOR 가능
                        .requestMatchers(HttpMethod.GET, "/api/codes/**", "/api/code-search-log").permitAll() // 코드 조회는 누구나 가능
                        .anyRequest().permitAll() // 나머지 요청은 인증 불필요
                )
                .addFilter(new CustomAuthenticationFilter(authenticationManager, jwtTokenProvider))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userRepository), CustomAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("http://localhost:*", "https://*.yourdomain.com") // 명시적인 패턴만 허용
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true); // allowCredentials를 true로 설정
            }
        };
    }
}
