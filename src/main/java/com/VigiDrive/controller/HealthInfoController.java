package com.VigiDrive.controller;

import com.VigiDrive.service.HealthInfoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HealthInfoController {

    private HealthInfoService healthInfoService;
}
