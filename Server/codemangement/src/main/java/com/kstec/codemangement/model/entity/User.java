package com.kstec.codemangement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "user_id", nullable = false, length = 36, updatable = false)
    private String userId;

    @Column(name = "login_id", nullable = false, length = 20)
    private String loginId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "user_name", nullable = false, length = 20)
    private String userName;

    @Column(name = "password", nullable = false, length = 255)
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "Password must contain at least one letter, one number, one special character, and be at least 8 characters long."
    )
    private String password;

    @Column(name = "activated", nullable = false)
    private boolean activated;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Code> codes; // codes 누가 등록하였는지, 해당 사용자가 어떤 codes 를 등록했는지 찾기 위한 양방향



    // Enum for user roles
    public enum Role {
        USER,
        ADMIN,
        OPERATOR
    }

    // Getters and Setters (생략 가능)
}
