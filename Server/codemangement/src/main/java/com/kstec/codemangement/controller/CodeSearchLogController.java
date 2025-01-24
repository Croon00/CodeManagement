package com.kstec.codemangement.controller;

import com.kstec.codemangement.model.dto.responsedto.CodeResponseDto;
import com.kstec.codemangement.service.CodeSearchLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/code-search-log")
@Tag(name = "Code Search Log API", description = "코드 검색 로그 관련 API")
public class CodeSearchLogController {

    @Autowired
    private CodeSearchLogService codeSearchLogService;

    /**
     * 지난 일주일 동안 가장 많이 검색된 코드 목록 반환
     * @return ResponseEntity<List<CodeResponseDto>>
     */
    @GetMapping("/popular")
    @Operation(
            summary = "인기 검색 코드 조회",
            description = "지난 일주일 동안 가장 많이 검색된 코드를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 결과 반환")
            }
    )
    public ResponseEntity<List<CodeResponseDto>> getPopularCodesLastWeek() {
        List<CodeResponseDto> popularCodes = codeSearchLogService.getPopularCodesLastWeek();
        return ResponseEntity.ok(popularCodes);
    }
}
