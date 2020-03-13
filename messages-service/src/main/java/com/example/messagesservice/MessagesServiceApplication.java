package com.example.messagesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class MessagesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagesServiceApplication.class, args);
	}

}
