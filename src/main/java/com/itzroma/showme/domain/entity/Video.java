package com.itzroma.showme.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

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
    private Set<User> likes;

    @ManyToMany(mappedBy = "dislikedVideos")
    private Set<User> dislikes;

    @OneToMany(mappedBy = "video")
    private Set<Comment> comments;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    public Video(String videoUrl, String previewUrl, String title, String description, User author) {
        this.videoUrl = videoUrl;
        this.previewUrl = previewUrl;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
