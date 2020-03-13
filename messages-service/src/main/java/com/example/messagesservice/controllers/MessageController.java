package com.example.messagesservice.controllers;

import com.example.messagesservice.models.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class MessageController {

    @GetMapping
    public Message getMessages(){
        return new Message(15, "hello world message");
    }

}









