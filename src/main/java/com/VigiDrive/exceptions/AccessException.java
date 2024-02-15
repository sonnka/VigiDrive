package com.VigiDrive.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

public class AccessException extends Exception {

    private final AccessExceptionProfile accessExceptionProfile;

    public AccessException(AccessExceptionProfile accessExceptionProfile) {
        super(accessExceptionProfile.exceptionMessage);
        this.accessExceptionProfile = accessExceptionProfile;
    }

    public String getName() {
        return accessExceptionProfile.exceptionName;
    }

    public HttpStatus getResponseStatus() {
        return accessExceptionProfile.responseStatus;
    }

    @AllArgsConstructor
    public enum AccessExceptionProfile {

        INVALID_ACCESS("invalid_access",
                "Access is invalid.", HttpStatus.BAD_REQUEST);


        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}