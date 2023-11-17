package com.VigiDrive.service;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.DriverRequest;
import com.VigiDrive.model.response.DriverDTO;

public interface DriverService {
    DriverDTO updateDriver(Long id, DriverRequest newDriver) throws UserException;
}
