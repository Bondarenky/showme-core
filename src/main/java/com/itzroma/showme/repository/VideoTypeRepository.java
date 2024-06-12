package com.itzroma.showme.repository;

import com.itzroma.showme.domain.entity.VideoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoTypeRepository extends JpaRepository<VideoType, String> {
    Optional<VideoType> findByName(String name);
}
