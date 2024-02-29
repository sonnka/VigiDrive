package com.VigiDrive.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MessageDTO {

    private Long messageId;

    private LocalDateTime time;

    private String text;

    private boolean me;
}
