package com.VigiDrive.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

public class AmazonException extends Exception {
    private final AmazonExceptionProfile amazonExceptionProfile;

    public AmazonException(AmazonExceptionProfile amazonExceptionProfile) {
        super(amazonExceptionProfile.exceptionMessage);
        this.amazonExceptionProfile = amazonExceptionProfile;
    }

    public String getName() {
        return amazonExceptionProfile.exceptionName;
    }

    public HttpStatus getResponseStatus() {
        return amazonExceptionProfile.responseStatus;
    }

    @AllArgsConstructor
    public enum AmazonExceptionProfile {
        SOMETHING_WRONG("invalid_uploading",
                "Something went wrong while uploading avatar.", HttpStatus.BAD_REQUEST);


        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}

