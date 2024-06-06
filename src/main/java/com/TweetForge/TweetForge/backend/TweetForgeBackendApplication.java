package com.TweetForge.TweetForge.backend;

import com.TweetForge.TweetForge.backend.models.Role;
import com.TweetForge.TweetForge.backend.repositories.RoleRepository;
import com.TweetForge.TweetForge.backend.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class TweetForgeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetForgeBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserService userService) {
		return args -> {
			roleRepository.save(new Role(1, "USER")); //creates a new Role object with an ID of 1 and an authority of "USER", and saves it to the db using the 'roleRepository'(obj)
//			ApplicationUser u = new ApplicationUser();
//
//			u.setFirstName("Khaira");
//			u.setLastName("Fuad");
//
//			userService.registerUser(u);

		};
	}
}
