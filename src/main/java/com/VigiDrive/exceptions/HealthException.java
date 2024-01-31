package com.VigiDrive.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

public class HealthException extends Exception {
    private final HealthExceptionProfile healthExceptionProfile;

    public HealthException(HealthExceptionProfile healthExceptionProfile) {
        super(healthExceptionProfile.exceptionMessage);
        this.healthExceptionProfile = healthExceptionProfile;
    }

    public String getName() {
        return healthExceptionProfile.exceptionName;
    }

    public HttpStatus getResponseStatus() {
        return healthExceptionProfile.responseStatus;
    }

    @AllArgsConstructor
    public enum HealthExceptionProfile {
        SOMETHING_WRONG("invalid_statistics",
                "Something went wrong while generating statistics.", HttpStatus.BAD_REQUEST);


        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}
