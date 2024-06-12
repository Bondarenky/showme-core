package com.itzroma.showme.repository;

import com.itzroma.showme.domain.entity.Video;
import com.itzroma.showme.domain.entity.VideoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {
    @Query("select v from Video v where (v.author.name ilike ?1 or v.title ilike ?1 or v.description ilike ?1) and ?2 member of v.videoTypes")
    List<Video> findBySearchTextAndType(String searchTest, VideoType videoType);
}
