package com.kstec.codemangement.model.Repository;

import com.kstec.codemangement.model.entity.CodeSearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeSearchLogRepository extends JpaRepository<CodeSearchLog, Long> {

    @Query(value = "SELECT code_id, COUNT(*) AS searchCount " +
            "FROM code_search_log " +
            "WHERE searched_at >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY) " +
            "GROUP BY code_id " +
            "ORDER BY searchCount DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findMostSearchedCodesLastWeek();
}

