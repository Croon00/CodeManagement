package com.kstec.codemangement.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_favorite_codes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFavoriteCodes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long favoriteId;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User 단방향

    @ManyToOne
    @JoinColumn(name = "code_id", nullable = false)
    private Code code; // Code 단방향
}
