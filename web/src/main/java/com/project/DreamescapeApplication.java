package com.project;

import com.project.domain.identity.Role;
import com.project.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class DreamescapeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamescapeApplication.class);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //@Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            //userService.addUser(new RegisterDto("miteva.h", "kiki", "kiki", "Hristina", "Miteva","USER"));
            //userService.addUser(new RegisterDto("delenikow", "stole", "stole", "Stojan", "Delenikov", "ADMIN"));

            userService.addRole(new Role("ADMIN"));
            userService.addRole(new Role("USER"));
//
//            userService.addRoleToUser("miteva.h", "USER");
//            userService.addRoleToUser("miteva.h", "ADMIN");
//            userService.addRoleToUser("delenikow", "ADMIN");
        };
    }
}
