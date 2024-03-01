package com.VigiDrive.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MessagesResponse {

    private Long receiverId;

    private String receiverFullName;

    private String avatar;

    private List<MessageDTO> chatMessages;
}
