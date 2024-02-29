package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Message;
import com.VigiDrive.model.request.MessageRequest;
import com.VigiDrive.model.response.MessageDTO;
import com.VigiDrive.model.response.MessagesResponse;
import com.VigiDrive.repository.MessageRepository;
import com.VigiDrive.repository.UserRepository;
import com.VigiDrive.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private UserRepository userRepository;
    private MessageRepository messageRepository;


    @Override
    public MessagesResponse getChatHistory(String email, Long userId, Long receiverId) throws UserException {
        var user = userRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.USER_NOT_FOUND)
        );

        if (!Objects.equals(user.getId(), userId)) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        var receiver = userRepository.findById(receiverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.RECEIVER_NOT_FOUND)
        );

        var receivedMessages = messageRepository.findAllByReceiverAndSenderOrderByTime(user, receiver);
        var sentMessages = messageRepository.findAllByReceiverAndSenderOrderByTime(receiver, user);

        receivedMessages.addAll(sentMessages);

        List<MessageDTO> chatMessages = receivedMessages.stream()
                .map(message -> toMessageDTO(message, userId))
                .sorted(Comparator.comparing(MessageDTO::getTime))
                .toList();

        return MessagesResponse.builder()
                .receiverFullName(receiver.getLastName() + " " + receiver.getFirstName())
                .chatMessages(chatMessages)
                .build();
    }

    @Override
    public MessageDTO sendMessage(String email, Long userId, Long receiverId, MessageRequest messageRequest)
            throws UserException {
        var user = userRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.USER_NOT_FOUND)
        );

        if (!Objects.equals(user.getId(), userId)) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        var receiver = userRepository.findById(receiverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.RECEIVER_NOT_FOUND)
        );

        return toMessageDTO(
                messageRepository.save(Message.builder()
                        .time(LocalDateTime.now())
                        .text(messageRequest.getText())
                        .sender(user)
                        .receiver(receiver)
                        .build()
                ),
                userId
        );
    }

    private MessageDTO toMessageDTO(Message message, Long userId) {
        return MessageDTO.builder()
                .messageId(message.getId())
                .time(message.getTime())
                .text(message.getText())
                .me(Objects.equals(message.getSender().getId(), userId))
                .build();
    }
}
