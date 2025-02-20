package com.kstec.codemangement.service;

import com.kstec.codemangement.model.document.CodeSearchLogDocument;
import com.kstec.codemangement.model.repository.CodeSearchLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CodeSearchLogServiceImpl implements CodeSearchLogService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private CodeSearchLogRepository codeSearchLogRepository;

    /**
     * 최근 7일간 검색된 기록을 최신순으로 가져오기 (최대 10개)
     */
    public List<CodeSearchLogDocument> getRecentSearchesLastWeek() {
        try {
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7); // ✅ 변경됨
            Criteria criteria = Criteria.where("searchedAt").greaterThanEqual(sevenDaysAgo);
            Query query = new CriteriaQuery(criteria);
            query.setPageable(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "searchedAt")));

            SearchHits<CodeSearchLogDocument> searchHits = elasticsearchOperations.search(query, CodeSearchLogDocument.class);

            return searchHits.getSearchHits().stream()
                    .map(hit -> hit.getContent())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }



    /**
     * ✅ 검색 로그 Batch Insert (최적화)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAllSearchLogs(List<CodeSearchLogDocument> logs) {
        if (logs == null || logs.isEmpty()) {
            return;
        }

        codeSearchLogRepository.saveAll(logs);
    }

    /**
     * 검색 로그 저장 (로그인된 사용자, 개별 저장)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSearchLogByUser(String userId, Long codeId, String codeName) {
        CodeSearchLogDocument searchLog = CodeSearchLogDocument.builder()
                .codeId(codeId)
                .codeName(codeName)
                .userId(userId)
                .searchedAt(LocalDateTime.now()) // ✅ 변경됨
                .build();

        codeSearchLogRepository.save(searchLog);
    }

    /**
     * 검색 로그 저장 (비로그인 사용자, 개별 저장)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSearchLog(Long codeId, String codeName) {
        CodeSearchLogDocument searchLog = CodeSearchLogDocument.builder()
                .codeId(codeId)
                .codeName(codeName)
                .userId(null)
                .searchedAt(LocalDateTime.now()) // ✅ 변경됨
                .build();

        codeSearchLogRepository.save(searchLog);
    }

}