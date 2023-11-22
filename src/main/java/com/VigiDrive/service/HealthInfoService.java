package com.VigiDrive.service;

import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;

public interface HealthInfoService {
    HealthInfoDTO addHealthInfo(Long driverId, HealthInfoRequest healthInfoRequest);

    HealthInfoDTO getCurrentHealthInfo(Long driverId);
}
