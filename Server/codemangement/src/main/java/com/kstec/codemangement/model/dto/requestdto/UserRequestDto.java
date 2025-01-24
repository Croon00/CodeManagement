package com.kstec.codemangement.model.dto.requestdto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    private String loginId;
    private String userName;
    private String password;
    private boolean activated;
    private String role;
    private LocalDateTime createdAt;

    // 특정 생성자 위에 @Builder 적용
    @Builder
    public UserRequestDto(String loginId, String userName, String password, boolean activated, String role, LocalDateTime createdAt) {
        this.loginId = loginId;
        this.userName = userName;
        this.password = password;
        this.activated = activated;
        this.role = role;
        this.createdAt = createdAt;
    }
}
