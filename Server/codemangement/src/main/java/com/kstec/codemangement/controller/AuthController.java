package com.kstec.codemangement.controller;

import com.kstec.codemangement.config.CustomUserDetails;
import com.kstec.codemangement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "토큰 갱신",
            description = "사용자의 Refresh 토큰을 이용하여 새로운 Access 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
                    @ApiResponse(responseCode = "401", description = "권한 없음")
            }
    )
    public ResponseEntity<?> refreshAccessToken() {
        try {
            String newAccessToken = authService.refreshAccessToken();
            return ResponseEntity.ok("{\"accessToken\": \"" + newAccessToken + "\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    @Operation(
            summary = "인증된 사용자 정보",
            description = "현재 인증된 사용자의 정보를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 정보 반환"),
                    @ApiResponse(responseCode = "401", description = "권한 없음")
            }
    )
    public ResponseEntity<?> getAuthenticatedUser() {
        try {
            CustomUserDetails userDetails = authService.getAuthenticatedUser();
            return ResponseEntity.ok(userDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
