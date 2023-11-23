package com.VigiDrive.service;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;

public interface HealthInfoService {
    HealthInfoDTO addHealthInfo(Long driverId, HealthInfoRequest healthInfoRequest) throws UserException;

    HealthInfoDTO getCurrentHealthInfo(Long driverId) throws UserException;
}
