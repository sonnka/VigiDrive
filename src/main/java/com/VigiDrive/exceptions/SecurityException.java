package com.VigiDrive.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

public class SecurityException extends Exception {
    private final SecurityExceptionProfile securityExceptionProfile;

    public SecurityException(SecurityExceptionProfile securityExceptionProfile) {
        super(securityExceptionProfile.exceptionMessage);
        this.securityExceptionProfile = securityExceptionProfile;
    }

    public String getName() {
        return securityExceptionProfile.exceptionName;
    }

    public HttpStatus getResponseStatus() {
        return securityExceptionProfile.responseStatus;
    }

    @AllArgsConstructor
    public enum SecurityExceptionProfile {

        REGISTRATION_FAILED("registration_failed",
                "Registration is failed. Try again later.", HttpStatus.UNAUTHORIZED),

        WRONG_AUTHENTICATION_DATA("wrong_authentication_data",
                "Wrong authentication data.", HttpStatus.UNAUTHORIZED),
        DELETING_FAILED("deleting_failed",
                "Something went wrong during deleting.", HttpStatus.BAD_REQUEST);


        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}