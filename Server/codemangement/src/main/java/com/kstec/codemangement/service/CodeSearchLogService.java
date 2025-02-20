package com.kstec.codemangement.service;

import com.kstec.codemangement.model.document.CodeSearchLogDocument;
import com.kstec.codemangement.model.dto.responsedto.CodeResponseDto;
import com.kstec.codemangement.model.entity.Code;

import java.util.List;

public interface CodeSearchLogService {
//
//    /**
//     * 지난 일주일 동안 가장 많이 검색된 코드 목록 조회
//     * @return List<CodeResponseDto>
//     */
    List<CodeSearchLogDocument> getRecentSearchesLastWeek();

    void saveSearchLogByUser(String userId, Long codeId, String codeName);
    void saveSearchLog(Long codeId, String codeName);
    void saveAllSearchLogs(List<CodeSearchLogDocument> logs);
}
