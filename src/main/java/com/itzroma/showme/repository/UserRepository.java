package com.itzroma.showme.repository;

import com.itzroma.showme.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE " +
            "WHERE u.id = ?1")
    void enable(String id);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.name = ?3, u.imageUrl = ?2 " +
            "WHERE u.id = ?1")
    void updateUser(String userId, String imageUrl, String name);
}
