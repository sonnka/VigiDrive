package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.Situation;
import com.VigiDrive.model.enums.SituationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SituationDTO {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private SituationType type;

    private String description;

    private String video;

    public SituationDTO(Situation situation) {
        this.id = situation.getId();
        this.start = situation.getStart();
        this.end = situation.getEnd();
        this.type = situation.getType();
        this.description = situation.getDescription();
        this.video = situation.getVideo();
    }
}
