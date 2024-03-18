package com.VigiDrive.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

public class MailException extends Exception {

    private final MailExceptionProfile mailExceptionProfile;

    public MailException(MailExceptionProfile mailExceptionProfile) {
        super(mailExceptionProfile.exceptionMessage);
        this.mailExceptionProfile = mailExceptionProfile;
    }

    public String getName() {
        return mailExceptionProfile.exceptionName;
    }

    public HttpStatus getResponseStatus() {
        return mailExceptionProfile.responseStatus;
    }

    @AllArgsConstructor
    public enum MailExceptionProfile {

        FAIL_SEND_MAIL("fail_send_email",
                "Failed to send email, please try again.", HttpStatus.BAD_GATEWAY);

        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}

