package com.itzroma.showme.repository;

import com.itzroma.showme.domain.entity.Video;
import com.itzroma.showme.domain.entity.VideoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {
    List<Video> findByAuthor_NameContainingIgnoreCaseOrTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndVideoTypesOrderByCreatedAt(
            String authorName, String title, String description, Set<VideoType> videoTypes);
}
