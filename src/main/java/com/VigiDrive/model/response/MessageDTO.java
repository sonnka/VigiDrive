package com.VigiDrive.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageDTO {

    private Long messageId;

    private String time;

    private String text;

    private boolean me;
}
