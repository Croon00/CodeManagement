//package com.kstec.codemangement.model.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.sql.Timestamp;
//
//@Entity
//@Table(name = "code_search_log")
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//public class CodeSearchLog {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "code_search_log_id", nullable = false)
//    private Long codeSearchLogId;
//
//    @CreationTimestamp
//    @Column(name = "searched_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//    private Timestamp searchedAt;
//
//    @Column(name = "code_id", nullable = false)
//    private Long codeId;
//
//    @Column(name = "code_name", nullable = false)
//    private String codeName;
//
//    @Column(name = "user_id", nullable = true)
//    private String userId;
//}
