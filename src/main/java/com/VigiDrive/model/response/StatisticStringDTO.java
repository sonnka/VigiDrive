package com.VigiDrive.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StatisticStringDTO {

    private String situation;

    private double amount;
}
