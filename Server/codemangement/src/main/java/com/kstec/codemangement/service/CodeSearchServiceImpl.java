package com.kstec.codemangement.service;

import com.kstec.codemangement.config.CustomUserDetails;
import com.kstec.codemangement.exception.BadRequestException;
import com.kstec.codemangement.model.repository.CodeSearchRepository;
import com.kstec.codemangement.model.document.CodeDocument;
import com.kstec.codemangement.model.document.CodeSearchLogDocument;
import com.kstec.codemangement.model.dto.responsedto.CodeResponseDto;
import com.kstec.codemangement.model.converter.CodeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CodeSearchServiceImpl implements CodeSearchService {

    private final CodeSearchRepository codeSearchRepository;
    private final CodeSearchLogService codeSearchLogService;

    @Autowired
    public CodeSearchServiceImpl(CodeSearchRepository codeSearchRepository, CodeSearchLogService codeSearchLogService) {
        this.codeSearchRepository = codeSearchRepository;
        this.codeSearchLogService = codeSearchLogService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodeResponseDto> searchCodes(String searchType, String query, String startDate, String endDate, Boolean activated, Long parentCodeId) {
        List<CodeDocument> codes;

        try {
            if ("codeName".equals(searchType)) {
                codes = codeSearchRepository.searchByCodeName(query);
            } else if ("codeValue".equals(searchType)) {
                codes = codeSearchRepository.searchByCodeValue(query);
            } else {
                codes = (List<CodeDocument>) codeSearchRepository.findAll();
            }
        } catch (Exception e) {
            throw new BadRequestException("🚨 검색 요청이 올바르지 않습니다.");
        }

        if (parentCodeId != null) {
            codes = codes.stream()
                    .filter(code -> code.getParentCodeId() != null && code.getParentCodeId().equals(parentCodeId))
                    .collect(Collectors.toList());
        }

        if (activated != null) {
            codes = codes.stream()
                    .filter(code -> code.getActivated().equals(activated))
                    .collect(Collectors.toList());
        }

        if (startDate != null && endDate != null) {
            try {
                LocalDateTime startDateTime = LocalDate.parse(startDate).atStartOfDay();
                LocalDateTime endDateTime = LocalDate.parse(endDate).atTime(23, 59, 59);

                codes = codes.stream()
                        .filter(code -> code.getCreatedAt().isAfter(startDateTime) && code.getCreatedAt().isBefore(endDateTime))
                        .collect(Collectors.toList());
            } catch (DateTimeParseException e) {
                throw new BadRequestException("🚨 날짜 형식이 잘못되었습니다. yyyy-MM-dd 형식을 사용하세요." + e);
            }
        }

        CustomUserDetails authenticatedUser = getAuthenticatedUserOrNull();

        if (authenticatedUser != null && !"anonymousUser".equals(authenticatedUser)) {
            List<CodeSearchLogDocument> logs = codes.stream()
                    .map(code -> new CodeSearchLogDocument(
                            authenticatedUser != null ? authenticatedUser.getUserId() : null, // ✅ 첫 번째 인자는 userId
                            code.getCodeId(),  // ✅ 두 번째 인자는 codeId
                            code.getCodeName(), // ✅ 세 번째 인자는 codeName
                            LocalDateTime.now() // ✅ 네 번째 인자는 searchedAt
                    ))
                    .collect(Collectors.toList());



            codeSearchLogService.saveAllSearchLogs(logs); // ✅ Batch Insert 적용
        } else {
            List<CodeSearchLogDocument> logs = codes.stream()
                    .map(code -> new CodeSearchLogDocument(
                            authenticatedUser != null ? authenticatedUser.getUserId() : null, // ✅ 첫 번째 인자는 userId
                            code.getCodeId(),  // ✅ 두 번째 인자는 codeId
                            code.getCodeName(), // ✅ 세 번째 인자는 codeName
                            LocalDateTime.now() // ✅ 네 번째 인자는 searchedAt
                    ))
                    .collect(Collectors.toList());

            System.out.println(LocalDateTime.now());
            codeSearchLogService.saveAllSearchLogs(logs); // ✅ 비로그인 사용자도 Batch Insert 적용
        }

        return codes.stream()
                .map(CodeConverter::toResponseDto)
                .collect(Collectors.toList());
    }

    private CustomUserDetails getAuthenticatedUserOrNull() {
        try {
            return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}
