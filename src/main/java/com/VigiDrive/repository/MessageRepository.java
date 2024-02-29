package com.VigiDrive.repository;

import com.VigiDrive.model.entity.Message;
import com.VigiDrive.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByReceiverAndSenderOrderByTime(User receiver, User sender);
}
