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

        USER_NOT_FOUND("user_not_found",
                "User is not found.", HttpStatus.NOT_FOUND),

        NOT_ADMIN("not_admin",
                "You are not admin.", HttpStatus.FORBIDDEN),

        NOT_MANAGER("not_manager",
                "You are not manager.", HttpStatus.FORBIDDEN),

        NOT_DRIVER("not_driver",
                "You are not driver.", HttpStatus.FORBIDDEN),

        FAIL_UPLOAD_AVATAR("fail_upload_avatar",
                "Failed to upload image, please try again.",
                HttpStatus.BAD_GATEWAY);

        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}
