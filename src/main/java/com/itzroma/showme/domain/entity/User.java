package com.itzroma.showme.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "author")
    private Set<Video> myVideos = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "liked_videos",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id"))
    private Set<Video> likedVideos = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "disliked_videos",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id"))
    private Set<Video> dislikedVideos = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "subscriptions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscribed_to_id"))
    private Set<User> subscriptions = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "history",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id"))
    private Set<Video> history = new LinkedHashSet<>();

    private Long subscribersCount = 0L;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void addSubscribers(int count) {
        subscribersCount += count;
    }

    public void incrementSubscribers() {
        subscribersCount++;
    }

    public void removeSubscribers(int count) {
        subscribersCount -= count;
    }

    public void decrementSubscribers() {
        subscribersCount--;
    }
}
