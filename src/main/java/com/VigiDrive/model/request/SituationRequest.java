package com.VigiDrive.model.request;

import com.VigiDrive.validation.DateTime;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SituationRequest {

    @NotEmpty
    @NotNull
    @DateTime
    private String start;

    @NotEmpty
    @NotNull
    @DateTime
    private String end;

    @NotEmpty
    @NotNull
    private String type;

    @NotEmpty
    @NotNull
    @Size(min = 6, max = 125)
    private String description;


    private String video;
}
