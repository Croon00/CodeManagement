package com.kstec.codemangement.service;

import com.kstec.codemangement.config.CustomUserDetails;
import com.kstec.codemangement.model.Repository.CodeRepository;
import com.kstec.codemangement.model.Repository.CodeSearchLogRepository;
import com.kstec.codemangement.model.Repository.UserRepository;
import com.kstec.codemangement.model.dto.requestdto.CodeRequestDto;
import com.kstec.codemangement.model.dto.responsedto.CodeResponseDto;
import com.kstec.codemangement.model.dto.responsedto.CodeSearchLogResponseDto;
import com.kstec.codemangement.model.entity.Code;
import com.kstec.codemangement.model.entity.CodeSearchLog;
import com.kstec.codemangement.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CodeSearchLogRepository codeSearchLogRepository;

    @Autowired
    private CodeSearchLogService codeSearchLogService;


    /**
     * 코드 조회 메서드
     * @param codeId
     * @return CodeResponseDto
     */
    @Override
    @Transactional(readOnly = true) // 읽기전용 트랜잭션
    public CodeResponseDto getCodeById(Long codeId) {
        Code code = codeRepository.findByIdWithParentCode(codeId); // fetch join 사용
        if (code == null) {
            throw new RuntimeException("Code not found with ID: " + codeId);
        }
        return toResponseDto(code);
    }


    /**
     * 코드 조회시 로그 남기기 위한 User 정보 확인 메서드
     * @return String
     */
    private String getAuthenticatedUserIdOrNull() {
        try {
            return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 모든 코드 조회
     * @return List<CodeResponseDto>
     */
    @Override
    @Transactional(readOnly = true)
    public List<CodeResponseDto> getAllCodes() {
        List<Code> codes = codeRepository.findAllWithParentCode(); // fetch join 사용
        return codes.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }


    /**
     * 코드 검색
     * @param searchType
     * @param query
     * @param startDate
     * @param endDate
     * @param activated
     * @param parentCodeId
     * @return List<CodeResponseDto>
     */
    @Override
    @Transactional// 읽기 전용 트랜잭션
    public List<CodeResponseDto> searchCodes(String searchType, String query, String startDate, String endDate, Boolean activated, Long parentCodeId) {
        List<Code> codes;

        // 검색 로직 동일
        if ("codeName".equals(searchType)) {
            codes = codeRepository.findByCodeNameContaining(query);
        } else if ("codeValue".equals(searchType)) {
            codes = codeRepository.findByCodeValue(query);
        } else {
            codes = codeRepository.findAllWithParentCode();
        }

        // 필터링 로직 동일
        if (parentCodeId != null) {
            codes = codes.stream()
                    .filter(code -> code.getParentCodeId() != null && code.getParentCodeId().getCodeId().equals(parentCodeId))
                    .collect(Collectors.toList());
        }

        if (activated != null) {
            codes = codes.stream()
                    .filter(code -> code.isActivated() == activated)
                    .collect(Collectors.toList());
        }

        if (startDate != null && endDate != null) {
            try {
                LocalDateTime start = LocalDateTime.parse(startDate);
                LocalDateTime end = LocalDateTime.parse(endDate);
                codes = codes.stream()
                        .filter(code -> code.getCreatedAt().isAfter(start) && code.getCreatedAt().isBefore(end))
                        .collect(Collectors.toList());
            } catch (DateTimeParseException e) {
                throw new RuntimeException("Invalid date format. Use: yyyy-MM-dd'T'HH:mm:ss", e);
            }
        }

        // 사용자 ID 확인
        String authenticatedUserId = getAuthenticatedUserIdOrNull();

        // **로그인된 사용자에 한해 검색 기록 저장**
        if (authenticatedUserId != null && !"anonymousUser".equals(authenticatedUserId)) {
            codes.forEach(code -> codeSearchLogService.saveSearchLog(authenticatedUserId, code));
        }

        return codes.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }


    /**
     * 코드 생성
     * @param codeRequestDto
     * @return CodeResponseDto
     */
    @Override
    @Transactional // 쓰기 작업이 포함된 트랜잭션
    public CodeResponseDto createCode(CodeRequestDto codeRequestDto) {
        // 1. 인증된 사용자 ID(UUID) 가져오기
        String authenticatedUserId = getAuthenticatedUserId();

        // 2. UserRepository를 통해 사용자 조회
        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + authenticatedUserId));

        // 3. Parent Code 조회 및 유효성 검사
        Code parentCode = null;
        if (codeRequestDto.getParentCodeId() != null) {
            parentCode = codeRepository.findById(codeRequestDto.getParentCodeId())
                    .orElseThrow(() -> new RuntimeException("Parent Code not found with ID: " + codeRequestDto.getParentCodeId()));
        }

        // 4. 새로운 Code 객체 생성
        Code code = new Code();
        code.setCodeName(codeRequestDto.getCodeName());
        code.setCodeValue(codeRequestDto.getCodeValue());
        code.setCodeMean(codeRequestDto.getCodeMean());
        code.setActivated(codeRequestDto.isActivated());
        code.setParentCodeId(parentCode);
        code.setUser(user);

        // 5. 저장 후 ResponseDto 반환
        return toResponseDto(codeRepository.save(code));
    }

    /**
     * 코드 생성시 필요한 유저 정보 조회 메서드
     * @return String
     */
    public String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUserId(); // CustomUserDetails에서 userId를 반환하는 메서드 호출
        }

        throw new IllegalStateException("인증된 사용자 정보가 없습니다.");
    }


    /**
     * 코드 수정 메서드
     * @param codeId
     * @param codeRequestDto
     * @return CodeResponseDto
     */
    @Override
    @Transactional // 쓰기 작업이 포함된 트랜잭션
    public CodeResponseDto updateCode(Long codeId, CodeRequestDto codeRequestDto) {
        Code code = codeRepository.findById(codeId)
                .orElseThrow(() -> new RuntimeException("Code not found with ID: " + codeId));

        code.setCodeName(codeRequestDto.getCodeName());
        code.setCodeValue(codeRequestDto.getCodeValue());
        code.setCodeMean(codeRequestDto.getCodeMean());
        code.setActivated(codeRequestDto.isActivated());

        if (codeRequestDto.getParentCodeId() != null) {
            Code parentCode = codeRepository.findById(codeRequestDto.getParentCodeId())
                    .orElseThrow(() -> new RuntimeException("Parent Code not found"));
            code.setParentCodeId(parentCode);
        }

        return toResponseDto(codeRepository.save(code));
    }




    private CodeResponseDto toResponseDto(Code code) {
        return CodeResponseDto.builder()
                .codeId(code.getCodeId())
                .codeName(code.getCodeName())
                .codeValue(code.getCodeValue())
                .parentCodeId(code.getParentCodeId() != null ? code.getParentCodeId().getCodeId() : null)
                .parentCodeName(code.getParentCodeId() != null ? code.getParentCodeId().getCodeName() : null) // 부모 코드 이름 추가
                .codeMean(code.getCodeMean())
                .activated(code.isActivated())
                .userId(code.getUser().getUserId())
                .createdAt(code.getCreatedAt())
                .updatedAt(code.getUpdatedAt())
                .build();
    }

    /**
     *
     * @param codeSearchLog
     * @return CodeSearchLogResponseDto
     */
    private CodeSearchLogResponseDto toResponseDto(CodeSearchLog codeSearchLog) {
        return CodeSearchLogResponseDto.builder()
                        .codeSearchLogId(codeSearchLog.getCodeSearchLogId())
                        .codeId(codeSearchLog.getCode().getCodeId())
                        .userId(codeSearchLog.getUser() != null ? codeSearchLog.getUser().getUserId() : null)
                        .searchedAt(codeSearchLog.getSearchedAt())
                .build();
    }
}
