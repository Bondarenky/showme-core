package com.itzroma.showme.repository;

import com.itzroma.showme.domain.entity.VerificationToken;
import com.itzroma.showme.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    Optional<VerificationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE VerificationToken t " +
            "SET t.confirmedAt = ?2 " +
            "WHERE t.token = ?1")
    void confirm(String token, LocalDateTime confirmedAt);

    void deleteByUser(User user);
}
