package com.kstec.codemangement.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private String userId; // UUID 또는 ID
    private String username; // 로그인 ID
    private String password; // 비밀번호 (JWT 인증 시 필요 없음)
    private Collection<? extends GrantedAuthority> authorities; // 권한

    // 기존 생성자
    public CustomUserDetails(String userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    // 비밀번호 없이 사용하는 생성자 추가
    public CustomUserDetails(String userId, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = null;
        this.password = null;
        this.authorities = authorities;
    }

    public String getUserId() {
        return userId;
    }

    // UserDetails 인터페이스 구현 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password; // 필요 없으면 null로 처리 가능
    }

    @Override
    public String getUsername() {
        return username; // JWT 인증에 필요하지 않다면 null 처리 가능
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
