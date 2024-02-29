package com.VigiDrive.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MessagesResponse {

    private String receiverFullName;

    private List<MessageDTO> chatMessages;
}
