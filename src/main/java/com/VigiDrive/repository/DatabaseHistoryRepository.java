package com.VigiDrive.repository;

import com.VigiDrive.model.entity.DatabaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseHistoryRepository extends JpaRepository<DatabaseHistory, Long> {
}