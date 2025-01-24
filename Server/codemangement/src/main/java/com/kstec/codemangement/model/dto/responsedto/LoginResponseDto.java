package com.kstec.codemangement.model.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDto {

    private String userId;    // 사용자 고유 ID
    private String userName;  // 사용자 이름
    private String role;      // 사용자 권한 (USER/ADMIN)
    private String jwtToken;  // JWT 토큰
    private String refreshToken;

    @Builder
    public LoginResponseDto(String userId, String userName, String role, String jwtToken, String refreshToken) {
        this.userId = userId;
        this.userName = userName;
        this.role = role;
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
    }
}
