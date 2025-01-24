package com.kstec.codemangement.model.dto.responsedto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

    private String userId;        // 고유 사용자 ID
    private String loginId;       // 사용자 로그인 ID
    private String userName;      // 사용자 이름
    private boolean activated;    // 활성화 여부
    private String role;          // 사용자 역할 (ADMIN, USER, OPERATOR 등)
    private LocalDateTime createdAt; // 생성일시

    // @Builder 적용
    @Builder
    public UserResponseDto(String userId, String loginId, String userName, boolean activated, String role, LocalDateTime createdAt) {
        this.userId = userId;
        this.loginId = loginId;
        this.userName = userName;
        this.activated = activated;
        this.role = role;
        this.createdAt = createdAt;
    }
}
