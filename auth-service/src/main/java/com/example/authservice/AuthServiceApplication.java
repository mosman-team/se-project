package com.example.authservice;

import com.example.authservice.models.ERole;
import com.example.authservice.models.Role;
import com.example.authservice.repos.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.event.EventListener;


@SpringBootApplication
@EnableEurekaClient
public class AuthServiceApplication {

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}


	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		roleRepository.save(new Role(ERole.ROLE_STUDENT));
		roleRepository.save(new Role(ERole.ROLE_TEACHER));
	}
}












