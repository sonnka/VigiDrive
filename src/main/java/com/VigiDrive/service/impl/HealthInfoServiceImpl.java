package com.VigiDrive.service.impl;

import com.VigiDrive.repository.HealthInfoRepository;
import com.VigiDrive.service.HealthInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HealthInfoServiceImpl implements HealthInfoService {

    private HealthInfoRepository healthInfoRepository;
}
