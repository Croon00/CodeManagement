package com.kstec.codemangement.model.repository;

import com.kstec.codemangement.model.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {

    // 코드 이름 포함 검색 (인덱스 사용)
    @Query("SELECT c FROM Code c LEFT JOIN FETCH c.parentCodeId WHERE c.codeName LIKE %:codeName%")
    List<Code> findByCodeNameContaining(String codeName); // 코드 이름 검색

    // 코드 값 포함 검색 (인덱스 사용)
    @Query("SELECT c FROM Code c LEFT JOIN FETCH c.parentCodeId WHERE c.codeValue LIKE %:codeValue%")
    List<Code> findByCodeValue(String codeValue); // 코드 값 검색

    // 부모 코드와 함께 검색
    @Query("SELECT c FROM Code c LEFT JOIN FETCH c.parentCodeId WHERE c.id = :codeId")
    Code findByIdWithParentCode(Long codeId);

    // 부모 코드와 함께 모두 검색
    @Query("SELECT c FROM Code c LEFT JOIN FETCH c.parentCodeId")
    List<Code> findAllWithParentCode();
}
