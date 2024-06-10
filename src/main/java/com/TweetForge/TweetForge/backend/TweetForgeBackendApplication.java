package com.TweetForge.TweetForge.backend;

import com.TweetForge.TweetForge.backend.config.RSAKeyProperties;
import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Role;
import com.TweetForge.TweetForge.backend.repositories.RoleRepository;
import com.TweetForge.TweetForge.backend.repositories.UserRepository;
import com.TweetForge.TweetForge.backend.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;


@EnableConfigurationProperties(RSAKeyProperties.class)
@SpringBootApplication
public class TweetForgeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetForgeBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserService userService, PasswordEncoder encoder, UserRepository userRepository) {
		return args -> {
			roleRepository.save(new Role(1, "USER"));

			Role r = roleRepository.save (new Role(1, "USER"));

			Set<Role> roles = new HashSet<>();

			roles.add(r);

			ApplicationUser u = new ApplicationUser();
			u.setAuthorities(roles);
			u.setFirstName("unknown");
			u.setLastName("koder");
			u.setEmail("unknownkoder@mail.com");
			u.setUsername("unknownKoder");
			u.setPhone("5555555555");
			u.setPassword(encoder.encode("password"));
			u.setEnabled(true);
			u.setVerifiedAccount(true);

			userRepository.save(u);

			//creates a new Role object with an ID of 1 and an authority of "USER", and saves it to the db using the 'roleRepository'(obj)
			/*ApplicationUser u = new ApplicationUser();

			u.setFirstName("Khaira");
			u.setLastName("Fuad");

			userService.registerUser(u);
*/
		};
	}
}
