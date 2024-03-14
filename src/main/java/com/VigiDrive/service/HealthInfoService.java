package com.VigiDrive.service;

import com.VigiDrive.exceptions.HealthException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.model.response.HealthStatistics;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface HealthInfoService {

    HealthInfoDTO addHealthInfo(Authentication auth, Long driverId, HealthInfoRequest healthInfoRequest)
            throws UserException;

    HealthInfoDTO getCurrentHealthInfo(Authentication auth, Long driverId) throws UserException;

    List<HealthInfoDTO> getWeekHealthInfo(Driver driver);

    HealthStatistics getWeekHealthStatistics(Authentication auth, Long driverId) throws UserException, HealthException;

    HealthStatistics getMonthHealthStatistics(Authentication auth, Long driverId) throws UserException, HealthException;

    HealthStatistics getYearHealthStatistics(Authentication auth, Long driverId) throws UserException, HealthException;
}
