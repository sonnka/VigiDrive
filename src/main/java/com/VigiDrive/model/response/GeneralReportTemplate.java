package com.VigiDrive.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GeneralReportTemplate {

    private String generalHealthStatus;

    private String amountOfSituations;

    private String mostFrequentSituation;
}
