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

        EMAIL_IS_OCCUPIED("email_is_occupied",
                "This email is already occupied.", HttpStatus.CONFLICT),

        BAD_EMAIL("bad_email",
                "Failed email verification.",
                HttpStatus.UNAUTHORIZED),

        INVALID_VERIFICATION_TOKEN("invalid_verification_token",
                "Provided verification token is invalid.", HttpStatus.UNAUTHORIZED),

        VERIFICATION_TOKEN_NOT_FOUND("verification_token_not_found",
                "Provided verification token is not found.", HttpStatus.BAD_REQUEST),

        EXPIRED_VERIFICATION_TOKEN("expired_verification_token",
                "You token is expired. Please, register again.", HttpStatus.UNAUTHORIZED),

        WRONG_AUTHENTICATION_DATA("wrong_authentication_data",
                "Wrong authentication data.", HttpStatus.BAD_REQUEST);


        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}