package com.VigiDrive.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

public class SituationException extends Exception {
    private final SituationExceptionProfile situationExceptionProfile;

    public SituationException(SituationExceptionProfile situationExceptionProfile) {
        super(situationExceptionProfile.exceptionMessage);
        this.situationExceptionProfile = situationExceptionProfile;
    }

    public String getName() {
        return situationExceptionProfile.exceptionName;
    }

    public HttpStatus getResponseStatus() {
        return situationExceptionProfile.responseStatus;
    }

    @AllArgsConstructor
    public enum SituationExceptionProfile {

        SITUATION_NOT_FOUND("situation_not_found",
                "Situation is not found.", HttpStatus.NOT_FOUND);


        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}
