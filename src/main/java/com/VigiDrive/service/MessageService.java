package com.VigiDrive.service;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.MessageRequest;
import com.VigiDrive.model.response.MessagesResponse;
import com.VigiDrive.model.response.UserResponse;

import java.util.List;

public interface MessageService {

    MessagesResponse getChatHistory(String email, Long userId, Long receiverId) throws UserException;

    MessagesResponse sendMessage(Long userId, Long receiverId, MessageRequest message)
            throws UserException;

    List<UserResponse> getChats(String email, Long userId) throws UserException;
}
