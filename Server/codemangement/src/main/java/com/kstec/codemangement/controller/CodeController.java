package com.kstec.codemangement.controller;

import com.kstec.codemangement.model.dto.requestdto.CodeRequestDto;
import com.kstec.codemangement.model.dto.responsedto.CodeResponseDto;
import com.kstec.codemangement.service.CodeService;
import com.kstec.codemangement.service.CodeSearchService; // ✅ 추가: Elasticsearch 검색 서비스
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/codes")
@Tag(name = "Code API", description = "코드 관리 관련 API")
public class CodeController {

    @Autowired
    private CodeService codeService;

    /**
     * 코드 상세 조회
     * @param codeId
     * @return CodeResponseDto
     */
    @GetMapping("/{codeId}")
    @Operation(
            summary = "코드 상세 조회",
            description = "코드 ID를 이용하여 코드 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "코드 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "코드를 찾을 수 없음")
            }
    )
    public ResponseEntity<CodeResponseDto> getCodeById(@PathVariable Long codeId) {
        CodeResponseDto response = codeService.getCodeById(codeId);
        return ResponseEntity.ok(response);
    }

    /**
     * 모든 코드 조회
     * @return List<CodeResponseDto>
     */
    @GetMapping
    @Operation(
            summary = "모든 코드 조회",
            description = "등록된 모든 코드를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "코드 조회 성공")
            }
    )
    public ResponseEntity<List<CodeResponseDto>> getAllCodes() {
        List<CodeResponseDto> response = codeService.getAllCodes();
        return ResponseEntity.ok(response);
    }


    /**
     * 코드 생성
     * @param codeRequestDto
     * @return CodeResponseDto
     */
    @PostMapping
    @Operation(
            summary = "코드 생성",
            description = "새로운 코드를 생성합니다. (RDBMS 기반)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "코드 생성 성공")
            }
    )
    public ResponseEntity<CodeResponseDto> createCode(@RequestBody CodeRequestDto codeRequestDto) {
        CodeResponseDto response = codeService.createCode(codeRequestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 코드 수정
     * @param codeId
     * @param codeRequestDto
     * @return CodeResponseDto
     */
    @PutMapping("/{codeId}")
    @Operation(
            summary = "코드 수정",
            description = "코드를 수정합니다. (RDBMS 기반)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "코드 수정 성공")
            }
    )
    public ResponseEntity<CodeResponseDto> updateCode(
            @PathVariable Long codeId,
            @RequestBody CodeRequestDto codeRequestDto
    ) {
        CodeResponseDto response = codeService.updateCode(codeId, codeRequestDto);
        return ResponseEntity.ok(response);
    }
}
