package com.VigiDrive.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class ManagerStatisticSituations {

    private LocalDate from;

    private LocalDate to;

    private String frequentSituation;

    private List<StatisticDTO> statistic;
}
