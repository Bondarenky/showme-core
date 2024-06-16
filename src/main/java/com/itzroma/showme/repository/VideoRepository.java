package com.itzroma.showme.repository;

import com.itzroma.showme.domain.entity.Video;
import com.itzroma.showme.domain.entity.VideoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {
    @Query("SELECT v FROM Video v WHERE v.author.name ILIKE %:searchText% OR v.title ILIKE %:searchText% OR v.description ILIKE %:searchText%")
    List<Video> findBySearchText(@Param("searchText") String searchText);

    @Query("SELECT v FROM Video v " +
            "JOIN v.author a " +
            "JOIN v.videoTypes vt " +
            "WHERE (a.name LIKE %:searchText% OR v.title LIKE %:searchText% OR v.description LIKE %:searchText%) " +
            "AND vt = :videoType")
    List<Video> findBySearchTextAndVideoType(@Param("searchText") String searchText,
                                             @Param("videoType") VideoType videoType);
}
