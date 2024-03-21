package com.VigiDrive.repository;

import com.VigiDrive.model.entity.DatabaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DatabaseHistoryRepository extends JpaRepository<DatabaseHistory, Long> {

    List<DatabaseHistory> findAllByTimeAfterOrderByTime(LocalDateTime time);
}