package com.kstec.codemangement.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "Code",
        indexes = {
                @Index(name = "idx_code_name", columnList = "codeName"), // 단일 컬럼 인덱스
                @Index(name = "idx_code_value", columnList = "codeValue") // 단일 컬럼 인덱스
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id", nullable = false)
    private Long codeId;

    @Column(name = "code_name", nullable = false, length = 100)
    private String codeName;

    @Column(name = "code_value", nullable = false, length = 100)
    private String codeValue;

    @Column(name = "code_mean", nullable = false)
    private String codeMean;

    @Column(name = "activated", nullable = false)
    private boolean activated;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_code_id")
    private Code parentCodeId;

    @OneToMany(mappedBy = "parentCodeId", fetch = FetchType.LAZY)
    private List<Code> childrenCodeId = new ArrayList<>();

}
