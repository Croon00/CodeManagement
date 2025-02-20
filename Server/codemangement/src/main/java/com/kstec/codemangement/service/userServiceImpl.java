package com.kstec.codemangement.service;

import com.kstec.codemangement.exception.BadRequestException;
import com.kstec.codemangement.model.repository.UserRepository;
import com.kstec.codemangement.model.dto.requestdto.UserRequestDto;
import com.kstec.codemangement.model.dto.responsedto.UserResponseDto;
import com.kstec.codemangement.model.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class userServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 PasswordEncoder

    public userServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * 유저등록
     * @param userRequestDto
     * @return
     */
    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        // 중복된 loginId 확인
        if (userRepository.existsByLoginId(userRequestDto.getLoginId())) {
            throw new BadRequestException("이미 존재하는 loginId입니다: " + userRequestDto.getLoginId()); // 예외처리
        }

        // User 엔티티 생성
        User user = new User();
        user.setLoginId(userRequestDto.getLoginId());
        user.setUserName(userRequestDto.getUserName());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword())); // 비밀번호 암호화
        user.setActivated(userRequestDto.isActivated());
        user.setRole(User.Role.valueOf(userRequestDto.getRole())); // 문자열을 Enum으로 변환
        user.setCreatedAt(LocalDateTime.now());

        // 사용자 저장
        User savedUser = userRepository.save(user);

        // UserResponseDto로 변환하여 반환
        return UserResponseDto.builder()
                .userId(savedUser.getUserId())
                .loginId(savedUser.getLoginId())
                .userName(savedUser.getUserName())
                .activated(savedUser.isActivated())
                .role(savedUser.getRole().name())
                .createdAt(savedUser.getCreatedAt())
                .build();
    }
}
