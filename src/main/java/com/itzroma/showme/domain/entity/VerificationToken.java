package com.itzroma.showme.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Entity
@Table(name = "verification_tokens")
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime expiresAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime confirmedAt;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        createdAt = LocalDateTime.now();
        expiresAt = createdAt.plus(900_000, ChronoUnit.MILLIS);
    }
}
