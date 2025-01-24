package com.kstec.codemangement.model.dto.responsedto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CodeResponseDto {

    private Long codeId;          // 코드 ID
    private String codeName;      // 코드 이름
    private String codeValue;       // 코드 값
    private Long parentCodeId;    // 부모 코드 ID (nullable)
    private String codeMean;      // 코드 의미
    private boolean activated;    // 활성화 여부
    private String userId;        // 등록한 사용자 ID
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime updatedAt; // 수정 시간
    private Long searchCount;     // 검색 횟수 추가
    private String parentCodeName; // 부모 코드 이름 추가


    @Builder
    public CodeResponseDto(Long codeId, String codeName, String codeValue, Long parentCodeId, String codeMean, String parentCodeName, boolean activated, String userId, LocalDateTime createdAt, LocalDateTime updatedAt, Long searchCount) {
        this.codeId = codeId;
        this.codeName = codeName;
        this.codeValue = codeValue;
        this.parentCodeId = parentCodeId;
        this.codeMean = codeMean;
        this.activated = activated;
        this.userId = userId;
        this.parentCodeName = parentCodeName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.searchCount = searchCount;
    }
}
