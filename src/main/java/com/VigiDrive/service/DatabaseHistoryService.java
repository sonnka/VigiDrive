package com.VigiDrive.service;

import com.VigiDrive.model.entity.Admin;
import com.VigiDrive.model.response.DatabaseHistoryDTO;

import java.util.List;

public interface DatabaseHistoryService {

    List<DatabaseHistoryDTO> getWeekDatabaseHistory();

    List<DatabaseHistoryDTO> getMonthDatabaseHistory();

    void saveExportOperation(Admin admin);

    void saveImportOperation(Admin admin);
}
