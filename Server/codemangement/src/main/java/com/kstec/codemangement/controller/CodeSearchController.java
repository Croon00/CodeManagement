package com.kstec.codemangement.controller;

import com.kstec.codemangement.model.dto.responsedto.CodeResponseDto;
import com.kstec.codemangement.service.CodeSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/codes")
@Tag(name = "Code API", description = "코드 관리 관련 API")
public class CodeSearchController {

    @Autowired
    private CodeSearchService codeSearchService; // ✅ 추가: Elasticsearch 기반 검색 서비스

    /**
     * 코드 검색 (Elasticsearch 기반)
     * @param searchType
     * @param query
     * @param startDate
     * @param endDate
     * @param activated
     * @param parentCodeId
     * @return
     */
    @GetMapping("/search")
    @Operation(
            summary = "코드 검색",
            description = "검색 조건을 이용하여 코드를 검색합니다. (Elasticsearch 기반)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    public ResponseEntity<?> searchCodes(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Boolean activated,
            @RequestParam(required = false) Long parentCodeId) {
        List<CodeResponseDto> response = codeSearchService.searchCodes(searchType, query, startDate, endDate, activated, parentCodeId);
        return ResponseEntity.ok(response);  // ❌ try-catch 제거, 서비스 계층에서 예외 발생 시 GlobalExceptionHandler에서 처리
    }
}
