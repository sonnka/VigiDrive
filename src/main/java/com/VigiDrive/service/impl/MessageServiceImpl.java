package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.Manager;
import com.VigiDrive.model.entity.Message;
import com.VigiDrive.model.entity.User;
import com.VigiDrive.model.request.MessageRequest;
import com.VigiDrive.model.response.MessageDTO;
import com.VigiDrive.model.response.MessagesResponse;
import com.VigiDrive.model.response.UserResponse;
import com.VigiDrive.repository.MessageRepository;
import com.VigiDrive.repository.UserRepository;
import com.VigiDrive.service.MessageService;
import com.VigiDrive.util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private AuthUtil authUtil;

    @Override
    public MessagesResponse getChatHistory(String email, Long userId, Long receiverId) throws UserException {
        var user = authUtil.findUserByEmailAndId(email, userId);

        var receiver = userRepository.findById(receiverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.RECEIVER_NOT_FOUND)
        );

        var receivedMessages = messageRepository.findAllByReceiverAndSenderOrderByTime(user, receiver);
        var sentMessages = messageRepository.findAllByReceiverAndSenderOrderByTime(receiver, user);

        receivedMessages.addAll(sentMessages);

        List<MessageDTO> chatMessages = receivedMessages.stream()
                .sorted(Comparator.comparing(Message::getTime))
                .map(message -> toMessageDTO(message, userId))
                .toList();

        return MessagesResponse.builder()
                .receiverId(receiverId)
                .receiverFullName(receiver.getFirstName() + " " + receiver.getLastName())
                .avatar(receiver.getAvatar())
                .chatMessages(chatMessages)
                .build();
    }

    @Override
    public MessagesResponse sendMessage(Long userId, Long receiverId, MessageRequest message)
            throws UserException {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.USER_NOT_FOUND)
        );

        var receiver = userRepository.findById(receiverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.RECEIVER_NOT_FOUND)
        );

        messageRepository.save(Message.builder()
                .time(LocalDateTime.now())
                .text(message.getText())
                .sender(user)
                .receiver(receiver)
                .build()
        );

        return getChatHistory(user.getEmail(), userId, receiverId);
    }

    @Override
    public List<UserResponse> getChats(String email, Long userId) throws UserException {
        var user = authUtil.findUserByEmailAndId(email, userId);

        var messages = messageRepository.findAllByReceiverOrSender(user, user);

        List<User> users = new ArrayList<>();

        for (Message message : messages) {
            var sender = message.getSender();
            var receiver = message.getReceiver();

            if (!Objects.equals(sender.getId(), user.getId())) {
                users.add(sender);
            } else if (!Objects.equals(receiver.getId(), user.getId())) {
                users.add(receiver);
            }
        }
        return users.stream().distinct().map(UserResponse::new).toList();
    }

    @Override
    public void creatNewChat(Driver driver, Manager manager) throws UserException {
        sendMessage(manager.getId(), driver.getId(), new MessageRequest(
                "Hi!\nMy name is " + manager.getFirstName() + " " + manager.getLastName() +
                        ", I am your new manager.\nNice to meet you!"
        ));
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
