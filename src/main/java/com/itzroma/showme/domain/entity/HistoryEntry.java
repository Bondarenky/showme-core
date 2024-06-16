package com.itzroma.showme.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@IdClass(HistoryEntryId.class)
@Entity
@Table(name = "history_entries")
public class HistoryEntry {
    @Id
    @Column(length = 36)
    private String userId;

    @Id
    @Column(length = 36)
    private String videoId;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;

    public HistoryEntry(String userId, String videoId) {
        this.userId = userId;
        this.videoId = videoId;
        this.timestamp = LocalDateTime.now();
    }
}
