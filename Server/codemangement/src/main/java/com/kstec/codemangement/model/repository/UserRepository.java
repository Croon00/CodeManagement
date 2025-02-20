package com.kstec.codemangement.model.repository;

import com.kstec.codemangement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLoginId(String loginId); // loginId를 기반으로 사용자 검색
    boolean existsByLoginId(String loginId); // loginId가 이미 존재하는지 확인
}
