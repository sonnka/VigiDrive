package com.VigiDrive.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class HealthStatistics {

    private double generalLevelForPeriod;

    private int worstPeriod;

    private int bestPeriod;

    private List<StatisticElement> statistics;
}
