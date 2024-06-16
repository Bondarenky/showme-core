package com.itzroma.showme.repository;

import com.itzroma.showme.domain.entity.HistoryEntry;
import com.itzroma.showme.domain.entity.HistoryEntryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryEntryRepository extends JpaRepository<HistoryEntry, HistoryEntryId> {
    List<HistoryEntry> findByUserId(String userId);
}
