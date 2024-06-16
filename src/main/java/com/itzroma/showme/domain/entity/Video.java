package com.itzroma.showme.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "videos")
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String videoUrl;

    @Column(nullable = false)
    private String previewUrl;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToMany(mappedBy = "likedVideos")
    private List<User> likes = new ArrayList<>();

    @ManyToMany(mappedBy = "dislikedVideos")
    private List<User> dislikes = new ArrayList<>();

    @OneToMany(mappedBy = "video")
    private List<Comment> comments = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(name = "video_video_types",
            joinColumns = @JoinColumn(name = "video_id"),
            inverseJoinColumns = @JoinColumn(name = "video_type_id"))
    private List<VideoType> videoTypes = new ArrayList<>();

    public Video(String videoUrl, String previewUrl, String title, String description, User author, List<VideoType> videoTypes) {
        this.videoUrl = videoUrl;
        this.previewUrl = previewUrl;
        this.title = title;
        this.description = description;
        this.author = author;
        this.videoTypes = videoTypes;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
