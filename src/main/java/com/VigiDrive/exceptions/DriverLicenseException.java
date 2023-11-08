package com.VigiDrive.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

public class DriverLicenseException extends Exception {
    private final DriverLicenseExceptionProfile driverLicenseExceptionProfile;

    public DriverLicenseException(DriverLicenseExceptionProfile driverLicenseExceptionProfile) {
        super(driverLicenseExceptionProfile.exceptionMessage);
        this.driverLicenseExceptionProfile = driverLicenseExceptionProfile;
    }

    public String getName() {
        return driverLicenseExceptionProfile.exceptionName;
    }

    public HttpStatus getResponseStatus() {
        return driverLicenseExceptionProfile.responseStatus;
    }

    @AllArgsConstructor
    public enum DriverLicenseExceptionProfile {

        DRIVER_LICENSE_NOT_FOUND("driver_license_not_found",
                "Driver license is not found.", HttpStatus.NOT_FOUND),

        IS_EXPIRED("driver_license_expired",
                "Driver license is expired.", HttpStatus.FORBIDDEN),

        INVALID_NUMBER("invalid_license_number",
                "Driver license number is invalid.", HttpStatus.FORBIDDEN);


        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}
