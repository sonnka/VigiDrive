package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.DatabaseHistory;
import com.VigiDrive.model.enums.DatabaseOperation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DatabaseHistoryDTO {

    private Long id;

    private String adminEmail;

    private LocalDateTime time;

    private DatabaseOperation operation;

    public DatabaseHistoryDTO(DatabaseHistory history) {
        this.id = history.getId();
        this.adminEmail = history.getAdminEmail();
        this.time = history.getTime();
        this.operation = history.getOperation();
    }
}
