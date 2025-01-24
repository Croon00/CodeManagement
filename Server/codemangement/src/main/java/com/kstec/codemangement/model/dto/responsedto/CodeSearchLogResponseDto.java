package com.kstec.codemangement.model.dto.responsedto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSearchLogResponseDto {

    private Long codeSearchLogId; // 검색 로그 ID
    private Long codeId; // 검색된 코드 ID
    private String userId; // 검색한 사용자 ID
    private LocalDateTime searchedAt; // 검색 시간
}
