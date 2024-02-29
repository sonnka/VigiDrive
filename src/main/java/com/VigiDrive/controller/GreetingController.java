package com.VigiDrive.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@AllArgsConstructor
public class GreetingController {
    private SimpMessagingTemplate template;


    @MessageMapping("/message")
    @SendTo("/broker")
    public String greeting(String message) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " - " + message;
    }
}