package com.kstec.codemangement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kstec.codemangement.model.dto.requestdto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;

        // 로그인 URL 설정
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequestDto loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword());
            return getAuthenticationManager().authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse login request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String userId = ((CustomUserDetails) authResult.getPrincipal()).getUserId();

        // AccessToken과 RefreshToken 생성
        String accessToken = jwtTokenProvider.createToken(userId, authResult.getAuthorities().toString());
        String refreshToken = jwtTokenProvider.createToken(userId, "ROLE_REFRESH");

        // RefreshToken을 HttpOnly, Secure 쿠키에 저장
        jakarta.servlet.http.Cookie refreshCookie = new jakarta.servlet.http.Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true); // JavaScript에서 접근 불가능
        refreshCookie.setSecure(true); // HTTPS 연결에서만 작동
        refreshCookie.setPath("/"); // 전체 도메인에서 유효
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 (초 단위)
        response.addCookie(refreshCookie);

        // AccessToken을 JSON 응답으로 반환
        response.setContentType("application/json");
        response.getWriter().write("{\"accessToken\": \"" + accessToken + "\"}");
        response.getWriter().flush();
    }

}
