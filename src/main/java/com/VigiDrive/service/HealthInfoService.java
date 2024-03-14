package com.VigiDrive.service;

import com.VigiDrive.exceptions.HealthException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.model.response.HealthStatistics;

import java.util.List;

public interface HealthInfoService {

    HealthInfoDTO addHealthInfo(String email, Long driverId, HealthInfoRequest healthInfoRequest)
            throws UserException;

    HealthInfoDTO getCurrentHealthInfo(String email, Long driverId) throws UserException;

    List<HealthInfoDTO> getWeekHealthInfo(Driver driver);

    HealthStatistics getWeekHealthStatistics(String email, Long driverId) throws UserException, HealthException;

    HealthStatistics getMonthHealthStatistics(String email, Long driverId) throws UserException, HealthException;

    HealthStatistics getYearHealthStatistics(String email, Long driverId) throws UserException, HealthException;
}
