package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.HistoryEntry;
import com.itzroma.showme.repository.HistoryEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryEntryServiceImpl implements HistoryEntryService {
    private final HistoryEntryRepository historyEntryRepository;

    @Override
    public void save(HistoryEntry historyEntry) {
        historyEntryRepository.save(historyEntry);
    }

    @Override
    public List<HistoryEntry> findByUserId(String userId) {
        return historyEntryRepository.findByUserId(userId);
    }
}
