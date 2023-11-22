package com.VigiDrive.service;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateDriverRequest;
import com.VigiDrive.model.response.DriverDTO;

public interface DriverService {
    DriverDTO updateDriver(Long id, UpdateDriverRequest newDriver) throws UserException;

    DriverDTO registerDriver(RegisterRequest newDriver);
}
