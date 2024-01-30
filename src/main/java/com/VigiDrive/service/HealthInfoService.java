package com.VigiDrive.service;

import com.VigiDrive.exceptions.HealthException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.model.response.HealthStatistics;
import org.springframework.security.core.Authentication;

public interface HealthInfoService {
    HealthInfoDTO addHealthInfo(Authentication auth, Long driverId, HealthInfoRequest healthInfoRequest)
            throws UserException;

    HealthInfoDTO getCurrentHealthInfo(Authentication auth, Long driverId) throws UserException;

    HealthStatistics getWeekHealthStatistics(Authentication auth, Long driverId) throws UserException, HealthException;
}
