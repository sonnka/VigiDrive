package com.VigiDrive.controller;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.MessageRequest;
import com.VigiDrive.model.response.MessagesResponse;
import com.VigiDrive.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class GreetingController {

    private MessageService messageService;

    @MessageMapping("/users/{user-id}/chats/{receiver-id}/message")
    @SendTo("/broker")
    public MessagesResponse sendMessage(MessageRequest message,
                                        @DestinationVariable("user-id") Long userId,
                                        @DestinationVariable("receiver-id") Long receiverId)
            throws UserException {
        return messageService.sendMessage(userId, receiverId, message);
    }
}