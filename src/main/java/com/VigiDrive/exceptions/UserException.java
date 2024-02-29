package com.VigiDrive.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

public class UserException extends Exception {

    private final UserExceptionProfile userExceptionProfile;

    public UserException(UserExceptionProfile userExceptionProfile) {
        super(userExceptionProfile.exceptionMessage);
        this.userExceptionProfile = userExceptionProfile;
    }

    public String getName() {
        return userExceptionProfile.exceptionName;
    }

    public HttpStatus getResponseStatus() {
        return userExceptionProfile.responseStatus;
    }

    @AllArgsConstructor
    public enum UserExceptionProfile {

        ADMIN_NOT_FOUND("admin_not_found",
                "Admin is not found.", HttpStatus.NOT_FOUND),

        DRIVER_NOT_FOUND("driver_not_found",
                "Driver is not found.", HttpStatus.NOT_FOUND),

        MANAGER_NOT_FOUND("manager_not_found",
                "Manager is not found.", HttpStatus.NOT_FOUND),

        NOT_ADMIN("not_admin",
                "You are not admin.", HttpStatus.FORBIDDEN),

        NOT_MANAGER("not_manager",
                "You are not manager.", HttpStatus.FORBIDDEN),

        NOT_DRIVER("not_driver",
                "You are not driver.", HttpStatus.FORBIDDEN),

        EMAIL_MISMATCH("email_mismatch",
                "Email provided does not match the user's email.", HttpStatus.FORBIDDEN),

        ACCESS_NOT_FOUND("access_not_found",
                "Access is not found.", HttpStatus.NOT_FOUND),

        PERMISSION_DENIED("permission_denied",
                "Permission denied.", HttpStatus.FORBIDDEN),

        ACCESS_NOT_EXPIRING("access_not_expiring",
                "Access is not expiring, so you can not extend it.", HttpStatus.BAD_REQUEST),

        INVALID_HEALTH_DATA("invalid_health_data",
                "Health data is invalid.", HttpStatus.BAD_REQUEST);

        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}
