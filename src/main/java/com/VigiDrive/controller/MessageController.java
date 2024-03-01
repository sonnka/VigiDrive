package com.VigiDrive.controller;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.response.MessagesResponse;
import com.VigiDrive.model.response.UserResponse;
import com.VigiDrive.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;

    @GetMapping("/users/{user-id}/chats/{receiver-id}")
    public MessagesResponse getChatHistory(Authentication auth,
                                           @PathVariable("user-id") Long userId,
                                           @PathVariable("receiver-id") Long receiverId) throws UserException {
        return messageService.getChatHistory(auth.getName(), userId, receiverId);
    }

    @GetMapping("/users/{user-id}/chats")
    public List<UserResponse> getChats(Authentication auth,
                                       @PathVariable("user-id") Long userId) throws UserException {
        return messageService.getChats(auth.getName(), userId);
    }
}
