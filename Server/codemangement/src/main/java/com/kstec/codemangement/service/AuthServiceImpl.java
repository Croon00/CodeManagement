package com.kstec.codemangement.service;

import com.kstec.codemangement.config.CustomUserDetails;
import com.kstec.codemangement.config.JwtTokenProvider;
import com.kstec.codemangement.exception.UnauthorizedException; // ✅ UnauthorizedException 임포트 추가
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String refreshAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UnauthorizedException("유효하지 않은 인증 정보입니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return jwtTokenProvider.createToken(userDetails.getUserId(), userDetails.getAuthorities().toString());
    }

    @Override
    public CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthorizedException("사용자가 인증되지 않았습니다.");
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        } else {
            throw new UnauthorizedException("유효하지 않은 인증 사용자입니다.");
        }
    }
}
