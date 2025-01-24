package com.kstec.codemangement.model.dto.requestdto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CodeSearchLogRequestDto {

    private Long codeId; // 검색된 코드 ID
    private String userId; // 검색한 사용자 ID (nullable)

    @Builder
    public CodeSearchLogRequestDto(Long codeId, String userId) {
        this.codeId = codeId;
        this.userId = userId;
    }
}
