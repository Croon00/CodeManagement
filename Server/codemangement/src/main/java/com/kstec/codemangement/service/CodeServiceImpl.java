package com.kstec.codemangement.service;

import com.kstec.codemangement.config.CustomUserDetails;
import com.kstec.codemangement.exception.BadRequestException;
import com.kstec.codemangement.model.converter.CodeConverter;
import com.kstec.codemangement.model.repository.CodeRepository;
import com.kstec.codemangement.model.repository.CodeSearchLogRepository;
import com.kstec.codemangement.model.repository.UserRepository;
import com.kstec.codemangement.model.dto.requestdto.CodeRequestDto;
import com.kstec.codemangement.model.dto.responsedto.CodeResponseDto;
import com.kstec.codemangement.model.entity.Code;
import com.kstec.codemangement.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.kstec.codemangement.model.converter.CodeConverter.toResponseDto;

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
            throw new EntityNotFoundException("해당 ID를 가진 코드를 찾을 수 없습니다: " + codeId);
        }
        return toResponseDto(code);
    }


    /**
     * 코드 조회시 로그 남기기 위한 User 정보 확인 메서드
     * @return String
     */
    private CustomUserDetails getAuthenticatedUserIdOrNull() {
        try {

            return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
                .map(CodeConverter::toResponseDto)
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

        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + authenticatedUserId));

        Code parentCode = null;
        if (codeRequestDto.getParentCodeId() != null) {
            parentCode = codeRepository.findById(codeRequestDto.getParentCodeId())
                    .orElseThrow(() -> new EntityNotFoundException("부모 코드를 찾을 수 없습니다: " + codeRequestDto.getParentCodeId()));
        }

//        // 중복 코드 검사
//        if (codeRepository.existsByCodeName(codeRequestDto.getCodeName())) {
//            throw new BadRequestException("이미 존재하는 코드 이름입니다: " + codeRequestDto.getCodeName());
//        }


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
                .orElseThrow(() -> new EntityNotFoundException("해당 코드 ID를 찾을 수 없습니다: " + codeId));

        code.setCodeName(codeRequestDto.getCodeName());
        code.setCodeValue(codeRequestDto.getCodeValue());
        code.setCodeMean(codeRequestDto.getCodeMean());
        code.setActivated(codeRequestDto.isActivated());

        if (codeRequestDto.getParentCodeId() != null) {
            Code parentCode = codeRepository.findById(codeRequestDto.getParentCodeId())
                    .orElseThrow(() -> new EntityNotFoundException("부모 코드를 찾을 수 없습니다."));
            code.setParentCodeId(parentCode);
        }

        return toResponseDto(codeRepository.save(code));
    }



}
