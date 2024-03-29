package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.AccessException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Access;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.enums.TimeDuration;
import com.VigiDrive.model.request.AccessRequest;
import com.VigiDrive.model.request.ExtendAccessRequest;
import com.VigiDrive.model.response.AccessDTO;
import com.VigiDrive.repository.AccessRepository;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.service.AccessService;
import com.VigiDrive.service.MessageService;
import com.VigiDrive.util.AuthUtil;
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
    private MessageService messageService;
    private AuthUtil authUtil;


    @Override
    public AccessDTO getAccessInfo(String email, Long driverId, Long accessId) throws UserException, AccessException {
        authUtil.findDriverByEmailAndId(email, driverId);
        var access = accessRepository.findById(accessId).orElseThrow(
                () -> new AccessException(AccessException.AccessExceptionProfile.INVALID_ACCESS));

        return toAccessDTO(access);
    }

    @Override
    public AccessDTO requestAccess(String email, Long managerId, AccessRequest access) throws UserException {
        var manager = authUtil.findManagerByEmailAndId(email, managerId);

        var driver = driverRepository.findByEmailIgnoreCase(access.getDriverEmail().trim())
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (driver.getManager() != null) {
            throw new UserException(UserException.UserExceptionProfile.FORBIDDEN);
        }

        TimeDuration duration = TimeDuration.valueOf(access.getAccessDuration().toUpperCase());

        return toAccessDTO(accessRepository.save(
                Access.builder()
                        .driver(driver)
                        .manager(manager)
                        .startDateOfAccess(null)
                        .accessDuration(duration)
                        .endDateOfAccess(null)
                        .isNew(true)
                        .isActive(false)
                        .isExpiring(false)
                        .build())
        );
    }

    @Override
    public AccessDTO giveAccess(String email, Long driverId, Long accessId) throws UserException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        var access = accessRepository.findById(accessId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.ACCESS_NOT_FOUND));

        if (!Objects.equals(driver.getId(), access.getDriver().getId())) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        TimeDuration duration = access.getAccessDuration();

        LocalDateTime startDate = LocalDateTime.now();

        LocalDateTime endDate = calculateEndDateOfAccess(startDate, duration);

        access.setStartDateOfAccess(startDate);
        access.setEndDateOfAccess(endDate);
        access.setNew(false);
        access.setActive(true);
        access.setExpiring(checkExpiring(endDate));

        var createdAccess = accessRepository.save(access);

        driver.setManager(access.getManager());

        driverRepository.save(driver);

        messageService.creatNewChat(driver, access.getManager());

        return toAccessDTO(createdAccess);
    }

    @Override
    public AccessDTO stopAccess(String email, Long driverId, Long accessId) throws UserException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);
        return toAccessDTO(deactivateAccess(driver, accessId));
    }

    private Access deactivateAccess(Driver driver, Long accessId) throws UserException {
        var access = accessRepository.findById(accessId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.ACCESS_NOT_FOUND));

        if (!Objects.equals(driver.getId(), access.getDriver().getId())) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        access.setStartDateOfAccess(null);
        access.setEndDateOfAccess(null);
        access.setNew(false);
        access.setActive(false);
        access.setExpiring(false);

        var updatedAccess = accessRepository.save(access);

        driver.setManager(null);

        driverRepository.save(driver);

        return updatedAccess;
    }

    @Override
    public AccessDTO extendAccess(String email, Long managerId, Long accessId, ExtendAccessRequest timeDuration)
            throws UserException {
        var manager = authUtil.findManagerByEmailAndId(email, managerId);

        var access = accessRepository.findById(accessId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.ACCESS_NOT_FOUND));

        if (!Objects.equals(manager.getId(), access.getManager().getId())) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        if (!access.isExpiring()) {
            throw new UserException(UserException.UserExceptionProfile.ACCESS_NOT_EXPIRING);
        }

        TimeDuration duration = TimeDuration.valueOf(timeDuration.getAccessDuration().toUpperCase());

        LocalDateTime startDate = LocalDateTime.now(Clock.systemUTC());

        LocalDateTime endDate = calculateEndDateOfAccess(startDate, duration);

        access.setStartDateOfAccess(startDate);
        access.setEndDateOfAccess(endDate);
        access.setNew(false);
        access.setActive(true);
        access.setExpiring(checkExpiring(endDate));
        access.setAccessDuration(duration);

        return toAccessDTO(accessRepository.save(access));
    }

    @Override
    public List<AccessDTO> getAllAccessRequestsByDriver(String email, Long driverId) throws UserException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        return driver.getAccesses().stream().map(this::checkActive)
                .filter(Access::isNew)
                .map(this::toAccessDTO)
                .toList();
    }

    @Override
    public List<AccessDTO> getAllInactiveAccessesByDriver(String email, Long driverId) throws UserException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        return driver.getAccesses().stream().map(this::checkActive)
                .filter(access -> !access.isActive() && !access.isNew())
                .map(this::toAccessDTO)
                .toList();
    }

    @Override
    public List<AccessDTO> getAllActiveAccessesByDriver(String email, Long driverId) throws UserException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        return driver.getAccesses().stream().map(this::checkActive)
                .filter(Access::isActive)
                .map(this::toAccessDTO)
                .toList();
    }

    @Override
    public List<AccessDTO> getAllSentAccessesByManager(String email, Long managerId) throws UserException {
        var manager = authUtil.findManagerByEmailAndId(email, managerId);

        return manager.getAccesses().stream().map(this::checkActive)
                .filter(Access::isNew)
                .map(this::toAccessDTO)
                .toList();
    }

    @Override
    public List<AccessDTO> getAllInactiveAccessesByManager(String email, Long managerId) throws UserException {
        var manager = authUtil.findManagerByEmailAndId(email, managerId);

        return manager.getAccesses().stream().map(this::checkActive)
                .filter(access -> !access.isActive())
                .map(this::toAccessDTO)
                .toList();
    }

    @Override
    public List<AccessDTO> getAllActiveAccessesByManager(String email, Long managerId) throws UserException {
        var manager = authUtil.findManagerByEmailAndId(email, managerId);

        return manager.getAccesses().stream().map(this::checkActive)
                .filter(Access::isActive)
                .map(this::toAccessDTO)
                .toList();
    }

    @Override
    public List<AccessDTO> getAllExpiringAccessesByManager(String email, Long managerId) throws UserException {
        var manager = authUtil.findManagerByEmailAndId(email, managerId);

        return manager.getAccesses().stream().map(this::checkActive)
                .filter(Access::isExpiring)
                .map(this::toAccessDTO)
                .toList();
    }


    private Boolean checkExpiring(LocalDateTime endAccess) {
        return endAccess.minusDays(3).isBefore(LocalDateTime.now(Clock.systemUTC()));
    }

    private Access checkActive(Access access) {
        if (access.isActive()) {
            if (access.getEndDateOfAccess().isAfter(LocalDateTime.now(Clock.systemUTC()))) {
                access.setActive(true);
                access.setExpiring(checkExpiring(access.getEndDateOfAccess()));
            } else {
                try {
                    access = deactivateAccess(access.getDriver(), access.getId());
                } catch (Exception e) {
                    System.err.println("Something was wrong with access with id = " + access.getId());
                }
            }
        }
        return access;
    }

    private LocalDateTime calculateEndDateOfAccess(LocalDateTime startAccess, TimeDuration duration) {
        long days = 0;
        switch (duration) {
            case DAY -> days = 1;
            case WEEK -> days = 7;
            case TWO_WEEKS -> days = 14;
            case MONTH -> days = 30;
            case SIX_MONTHS -> days = 180;
            case YEAR -> days = 360;
        }
        return startAccess.plusDays(days);
    }

    private AccessDTO toAccessDTO(Access access) {

        return AccessDTO.builder()
                .id(access.getId())
                .driverEmail(access.getDriver().getEmail())
                .managerEmail(access.getManager().getEmail())
                .startDateOfAccess(access.getStartDateOfAccess())
                .endDateOfAccess(access.getEndDateOfAccess())
                .accessDuration(access.getAccessDuration())
                .isActive(access.isActive())
                .isExpiring(access.isExpiring())
                .build();
    }
}
