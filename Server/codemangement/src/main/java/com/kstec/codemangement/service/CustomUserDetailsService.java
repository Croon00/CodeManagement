package com.kstec.codemangement.service;

import com.kstec.codemangement.config.CustomUserDetails;
import com.kstec.codemangement.model.Repository.UserRepository;
import com.kstec.codemangement.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login ID: " + username));

        return new CustomUserDetails(
                user.getUserId(),
                user.getLoginId(),
                user.getPassword(),
                Collections.emptyList() // 권한 추가 시 이곳에 설정
        );
    }
}
