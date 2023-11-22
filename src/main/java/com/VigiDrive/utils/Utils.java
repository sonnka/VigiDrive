package com.VigiDrive.utils;

import com.VigiDrive.model.entity.Access;
import com.VigiDrive.model.enums.TimeDuration;
import com.VigiDrive.model.response.AccessDTO;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.ManagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class Utils {

    private ManagerRepository managerRepository;

    private DriverRepository driverRepository;

    public static Boolean checkExpiring(LocalDateTime endAccess) {
        return endAccess.minusDays(3).isBefore(LocalDateTime.now(Clock.systemUTC()));
    }

    public static LocalDateTime calculateEndDateOfAccess(LocalDateTime startAccess, TimeDuration duration) {
        long days = 0;
        switch (duration) {
            case DAY -> days = 1;
            case WEEK -> days = 7;
            case TWO_WEEKS -> days = 14;
            case MONTH -> days = 30;
            case SIX_MONTH -> days = 180;
            case YEAR -> days = 360;
        }
        return startAccess.plusDays(days);
    }

    public AccessDTO toAccessDTO(Access access) {
        var manager = managerRepository.findById(access.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found!"));

        var driver = driverRepository.findById(access.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        return AccessDTO.builder()
                .id(access.getId())
                .driverEmail(driver.getEmail())
                .managerEmail(manager.getEmail())
                .startDateOfAccess(access.getStartDateOfAccess())
                .endDateOfAccess(access.getEndDateOfAccess())
                .accessDuration(access.getAccessDuration())
                .isActive(access.getIsActive())
                .isExpiring(access.getIsExpiring())
                .build();
    }
}
