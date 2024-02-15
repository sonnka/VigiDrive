package com.VigiDrive.model.response;

import com.VigiDrive.model.enums.SituationType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SituationStatistics {

    private int amountOfSituations;

    private SituationType mostFrequentSituation;

    private int mostFrequentPeriod;

    private List<StatisticElement> statistics;
}
