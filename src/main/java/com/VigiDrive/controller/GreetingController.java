package com.VigiDrive.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {


    @MessageMapping("/message")
    @SendTo("/broker/messages")
    public String greeting(String message) {
        System.out.println("Message: " + message);
        return "RE: " + message;
    }
}