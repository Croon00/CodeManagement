package com.kstec.codemangement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey; // SecretKey 객체 사용
    private final long validityInMilliseconds;
    private static final long REFRESH_TOKEN_EXPIRATION = TimeUnit.DAYS.toMillis(7); // 7일 (밀리초)

    // 생성자 주입 방식
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.expiration}") long validityInMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes()); // SecretKey 객체 생성
        this.validityInMilliseconds = validityInMilliseconds;
    }

    // JWT 토큰 생성
    public String createToken(String userId, String role) {
        Claims claims = Jwts.claims().setSubject(userId); // Subject 설정
        claims.put("role", role); // Custom Claims 설정

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256) // SecretKey로 서명
                .compact();
    }



    public String createRefreshToken(String userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION); // 예: 7일

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 Claims 추출
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // SecretKey로 서명 키 설정
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰에서 사용자 ID 추출
    public String getUserId(String token) {
        return getClaims(token).getSubject();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey) // SecretKey로 서명 키 설정
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage()); // 디버깅 로그

            return false;
        }
    }
}
