package com.VigiDrive.service.impl;

import com.VigiDrive.model.entity.Access;
import com.VigiDrive.model.enums.TimeDuration;
import com.VigiDrive.model.request.AccessRequest;
import com.VigiDrive.model.request.ExtendAccessRequest;
import com.VigiDrive.model.response.AccessDTO;
import com.VigiDrive.repository.AccessRepository;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.ManagerRepository;
import com.VigiDrive.service.AccessService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AccessServiceImpl implements AccessService {

    private AccessRepository accessRepository;
    private DriverRepository driverRepository;
    private ManagerRepository managerRepository;

    @Override
    public AccessDTO requestAccess(Long managerId, AccessRequest access) {
        // manager
        var driver = driverRepository.findByEmail(access.getDriverEmail().trim())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        TimeDuration duration = TimeDuration.valueOf(access.getAccessDuration().toUpperCase());


        return toAccessDTO(accessRepository.save(
                Access.builder()
                        .driverId(driver.getId())
                        .managerId(manager.getId())
                        .startDateOfAccess(null)
                        .accessDuration(duration)
                        .endDateOfAccess(null)
                        .isActive(false)
                        .isExpiring(false)
                        .build())
        );
    }

    @Override
    public AccessDTO giveAccess(Long driverId, Long accessId) {
        //driver
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        var access = accessRepository.findById(accessId)
                .orElseThrow(() -> new RuntimeException("Access not found"));

        if (!Objects.equals(driver.getId(), access.getDriverId())) {
            throw new RuntimeException("Permission denied");
        }

        TimeDuration duration = access.getAccessDuration();

        LocalDateTime startDate = LocalDateTime.now(Clock.systemUTC());

        LocalDateTime endDate = calculateEndDateOfAccess(startDate, duration);

        access.setStartDateOfAccess(startDate);
        access.setEndDateOfAccess(endDate);
        access.setIsActive(true);
        access.setIsExpiring(checkExpiring(endDate));

        return toAccessDTO(accessRepository.save(access));
    }

    @Override
    public AccessDTO stopAccess(Long driverId, Long accessId) {
        // driver
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        var access = accessRepository.findById(accessId)
                .orElseThrow(() -> new RuntimeException("Access not found"));

        if (!Objects.equals(driver.getId(), access.getDriverId())) {
            throw new RuntimeException("Permission denied");
        }

        access.setStartDateOfAccess(null);
        access.setEndDateOfAccess(null);
        access.setIsActive(false);
        access.setIsExpiring(false);

        return toAccessDTO(accessRepository.save(access));
    }

    @Override
    public AccessDTO extendAccess(Long managerId, Long accessId, ExtendAccessRequest timeDuration) {
        // manager
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        var access = accessRepository.findById(accessId)
                .orElseThrow(() -> new RuntimeException("Access not found"));

        if (!Objects.equals(manager.getId(), access.getManagerId())) {
            throw new RuntimeException("Permission denied");
        }

        if (!access.getIsExpiring()) {
            throw new RuntimeException("Permission denied");
        }

        TimeDuration duration = TimeDuration.valueOf(timeDuration.getAccessDuration().toUpperCase());

        LocalDateTime startDate = LocalDateTime.now(Clock.systemUTC());

        LocalDateTime endDate = calculateEndDateOfAccess(startDate, duration);

        access.setStartDateOfAccess(startDate);
        access.setEndDateOfAccess(endDate);
        access.setIsActive(true);
        access.setIsExpiring(checkExpiring(endDate));

        return toAccessDTO(accessRepository.save(access));
    }

    @Override
    public List<AccessDTO> getAllInactiveAccessesByDriver(Long driverId) {
        //driver
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return accessRepository.findAllByDriverIdAndIsActive(driver.getId(), false)
                .stream().map(this::toAccessDTO).toList();
    }

    @Override
    public List<AccessDTO> getAllActiveAccessesByDriver(Long driverId) {
        //driver
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return accessRepository.findAllByDriverIdAndIsActive(driver.getId(), true)
                .stream().map(this::toAccessDTO).toList();
    }

    @Override
    public List<AccessDTO> getAllInactiveAccessesByManager(Long managerId) {
        //manager
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        return accessRepository.findAllByManagerIdAndIsActive(manager.getId(), false)
                .stream().map(this::toAccessDTO).toList();
    }

    @Override
    public List<AccessDTO> getAllActiveAccessesByManager(Long managerId) {
        //manager
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        return accessRepository.findAllByManagerIdAndIsActive(manager.getId(), true)
                .stream().map(this::toAccessDTO).toList();
    }

    @Override
    public List<AccessDTO> getAllExpiringAccessesByManager(Long managerId) {
        //manager
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        return accessRepository.findAllByManagerIdAndIsExpiring(manager.getId(), true)
                .stream().map(this::toAccessDTO).toList();
    }


    private Boolean checkExpiring(LocalDateTime endAccess) {
        return endAccess.minusDays(3).isBefore(LocalDateTime.now(Clock.systemUTC()));
    }

    private LocalDateTime calculateEndDateOfAccess(LocalDateTime startAccess, TimeDuration duration) {
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

    private AccessDTO toAccessDTO(Access access) {
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
