package com.VigiDrive.service;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;
import org.springframework.security.core.Authentication;

public interface HealthInfoService {
    HealthInfoDTO addHealthInfo(Authentication auth, Long driverId, HealthInfoRequest healthInfoRequest)
            throws UserException;

    HealthInfoDTO getCurrentHealthInfo(Authentication auth, Long driverId) throws UserException;

}
