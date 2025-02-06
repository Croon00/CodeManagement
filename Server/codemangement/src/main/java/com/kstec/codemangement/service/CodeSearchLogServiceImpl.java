package com.kstec.codemangement.service;

import com.kstec.codemangement.model.Repository.CodeRepository;
import com.kstec.codemangement.model.Repository.CodeSearchLogRepository;
import com.kstec.codemangement.model.Repository.UserRepository;
import com.kstec.codemangement.model.dto.responsedto.CodeResponseDto;
import com.kstec.codemangement.model.entity.Code;
import com.kstec.codemangement.model.entity.CodeSearchLog;
import com.kstec.codemangement.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CodeSearchLogServiceImpl implements CodeSearchLogService {

    @Autowired
    private CodeSearchLogRepository codeSearchLogRepository;

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CodeResponseDto> getPopularCodesLastWeek() {
        List<Object[]> results = codeSearchLogRepository.findMostSearchedCodesLastWeek();

        return results.stream()
                .map(result -> {
                    Long codeId = (Long) result[0];
                    Long searchCount = (Long) result[1];

                    Code code = codeRepository.findById(codeId)
                            .orElseThrow(() -> new RuntimeException("Code not found with ID: " + codeId));

                    return CodeResponseDto.builder()
                            .codeId(code.getCodeId())
                            .codeName(code.getCodeName())
                            .codeValue(code.getCodeValue())
                            .searchCount(searchCount) // 검색 횟수 추가
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 데이터 저장을 위한 쓰기 가능한 트랜잭션
    public void saveSearchLogByUser(String userId, Code code) {
        CodeSearchLog searchLog = new CodeSearchLog();
        searchLog.setCode(code);

        // 사용자 정보 설정
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            searchLog.setUser(user);
        }

        codeSearchLogRepository.save(searchLog); // 로그 저장
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)// 데이터 저장을 위한 쓰기 가능한 트랜잭션
    public void saveSearchLog(Code code) {
        CodeSearchLog searchLog = new CodeSearchLog();
        searchLog.setCode(code);

        codeSearchLogRepository.save(searchLog);
    }

}
