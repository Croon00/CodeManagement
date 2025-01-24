package com.kstec.codemangement.model.dto.requestdto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CodeRequestDto {

    private String codeName;      // 코드 이름
    private String codeValue;       // 코드 값
    private Long parentCodeId;    // 부모 코드 ID (nullable)
    private String codeMean;      // 코드 의미
    private boolean activated;    // 활성화 여부
    private String userId;        // 등록한 사용자 ID
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime updatedAt; // 수정 시간

    @Builder
    public CodeRequestDto(String codeName, String codeValue, Long parentCodeId, String codeMean, boolean activated, String userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.codeName = codeName;
        this.codeValue = codeValue;
        this.parentCodeId = parentCodeId;
        this.codeMean = codeMean;
        this.activated = activated;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
