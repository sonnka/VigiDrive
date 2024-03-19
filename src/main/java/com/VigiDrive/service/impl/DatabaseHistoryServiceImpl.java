package com.VigiDrive.service.impl;

import com.VigiDrive.model.entity.Admin;
import com.VigiDrive.model.entity.DatabaseHistory;
import com.VigiDrive.model.enums.DatabaseOperation;
import com.VigiDrive.model.response.DatabaseHistoryDTO;
import com.VigiDrive.repository.DatabaseHistoryRepository;
import com.VigiDrive.service.DatabaseHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class DatabaseHistoryServiceImpl implements DatabaseHistoryService {

    private DatabaseHistoryRepository databaseHistoryRepository;

    @Override
    public List<DatabaseHistoryDTO> getWeekDatabaseHistory() {
        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1L);

        return databaseHistoryRepository.findAllByTimeAfterOrderByTime(startOfWeek)
                .stream()
                .map(DatabaseHistoryDTO::new)
                .toList();
    }

    @Override
    public List<DatabaseHistoryDTO> getMonthDatabaseHistory() {
        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfMonth = today.minusDays(today.getDayOfMonth() - 1L);

        return databaseHistoryRepository.findAllByTimeAfterOrderByTime(startOfMonth)
                .stream()
                .map(DatabaseHistoryDTO::new)
                .toList();
    }

    @Override
    public void saveExportOperation(Admin admin) {
        databaseHistoryRepository.save(DatabaseHistory.builder()
                .adminEmail(admin.getEmail())
                .time(LocalDateTime.now())
                .operation(DatabaseOperation.EXPORT)
                .build());
    }
}
