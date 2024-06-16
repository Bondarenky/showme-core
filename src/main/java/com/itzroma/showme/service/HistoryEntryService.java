package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.HistoryEntry;

import java.util.List;

public interface HistoryEntryService {
    void save(HistoryEntry historyEntry);

    List<HistoryEntry> findByUserId(String userId);
}
