package com.kstec.codemangement.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "code_search_log")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CodeSearchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_search_log_id", nullable = false)
    private Long codeSearchLogId;

    @CreationTimestamp
    @Column(name = "searched_at", nullable = false)
    private LocalDateTime searchedAt; // 검색 시간


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", nullable = false)
    private Code code; // 검색된 코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 검색한 사용자
}



