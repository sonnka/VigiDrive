package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.exceptions.UserException.UserExceptionProfile;
import com.VigiDrive.model.request.DriverRequest;
import com.VigiDrive.model.response.DriverDTO;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.service.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

    private DriverRepository driverRepository;

    public DriverDTO updateDriver(Long id, DriverRequest newDriver) throws UserException {
        var driver = driverRepository.findById(id).orElseThrow(
                () -> new UserException(UserExceptionProfile.USER_NOT_FOUND));

        if (newDriver != null) {
            driver.setName(newDriver.getName());
            driver.setSurname(newDriver.getSurname());
            driver.setDateOfBirth(newDriver.getDateOfBirth());
            driver.setSex(newDriver.getSex());
            driver.setCountryCode(newDriver.getCountryCode());
            driver.setPhoneNumber(newDriver.getPhoneNumber());
            driver.setEmergencyContact(newDriver.getEmergencyContact());
        }

        return new DriverDTO(driverRepository.save(driver));
    }
}
