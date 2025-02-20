package com.kstec.codemangement.model.converter;

import com.kstec.codemangement.model.document.CodeDocument;
import com.kstec.codemangement.model.dto.responsedto.CodeResponseDto;
import com.kstec.codemangement.model.entity.Code;

public class CodeConverter {
    public static CodeResponseDto toResponseDto(CodeDocument codeDocument) {
        return CodeResponseDto.builder()
                .codeId(codeDocument.getCodeId())
                .codeName(codeDocument.getCodeName())
                .codeValue(codeDocument.getCodeValue())
                .parentCodeId(codeDocument.getParentCodeId())
                .parentCodeName(null) // ✅ `CodeDocument`에는 `parentCodeName` 정보가 없음
                .codeMean(codeDocument.getCodeMean())
                .activated(codeDocument.getActivated())
                .userId(null) // ✅ `CodeDocument`에는 `User` 정보가 없으므로 null
                .createdAt(codeDocument.getCreatedAt())
                .updatedAt(codeDocument.getUpdatedAt())
                .searchCount(0L) // ✅ 검색 횟수 기본값 설정 (필요 시 수정)
                .build();
    }

    public static CodeResponseDto toResponseDto(Code code) {
        return CodeResponseDto.builder()
                .codeId(code.getCodeId())
                .codeName(code.getCodeName())
                .codeValue(code.getCodeValue())
                .parentCodeId(code.getParentCodeId() != null ? code.getParentCodeId().getCodeId() : null)
                .parentCodeName(code.getParentCodeId() != null ? code.getParentCodeId().getCodeName() : null)
                .codeMean(code.getCodeMean())
                .activated(code.isActivated())
                .userId(code.getUser().getUserId())
                .createdAt(code.getCreatedAt())
                .updatedAt(code.getUpdatedAt())
                .build();
    }
}
