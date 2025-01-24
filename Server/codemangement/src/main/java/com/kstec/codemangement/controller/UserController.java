package com.kstec.codemangement.controller;


import com.kstec.codemangement.config.JwtTokenProvider;
import com.kstec.codemangement.model.dto.requestdto.UserRequestDto;
import com.kstec.codemangement.model.dto.responsedto.UserResponseDto;
import com.kstec.codemangement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/user")
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(
            summary = "사용자 등록",
            description = "새로운 사용자를 등록합니다. ADMIN 권한이 필요합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 등록 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "401", description = "토큰 검증 실패")
            }
    )
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            // SecurityContext에서 인증 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);

            // ADMIN 권한 확인
            if (!"ROLE_ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            // 유저 등록 처리
            UserResponseDto response = userService.registerUser(userRequestDto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 검증 실패");
        }
    }
}
