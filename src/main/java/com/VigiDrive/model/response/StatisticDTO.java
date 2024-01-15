package com.VigiDrive.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StatisticDTO {

    private int dayOfWeek;

    private double amount;
}
