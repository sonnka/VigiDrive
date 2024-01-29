package com.VigiDrive.model.response;

import com.VigiDrive.model.enums.SituationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SituationStatistics {

    private int amountOfSituations;

    private SituationType mostFrequentSituation;

    private int mostFrequentPeriod;

    private List<StatisticElement> statistics;
}
