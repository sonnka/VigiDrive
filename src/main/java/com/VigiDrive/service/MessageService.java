package com.VigiDrive.service;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.MessageRequest;
import com.VigiDrive.model.response.MessageDTO;
import com.VigiDrive.model.response.MessagesResponse;

public interface MessageService {

    MessagesResponse getChatHistory(String email, Long userId, Long receiverId) throws UserException;

    MessageDTO sendMessage(String email, Long userId, Long receiverId, MessageRequest messageRequest)
            throws UserException;
}
