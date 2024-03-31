package com.atwaha.springldap;

import com.atwaha.springldap.model.User;
import com.atwaha.springldap.model.enums.Role;
import com.atwaha.springldap.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringLdapApplication implements ApplicationRunner {

    private final UserRepository userRepository;

    public SpringLdapApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringLdapApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User abdulRazak = User
                .builder()
                .username("abdul-razak.twaha")
                .name("Abdul-razak Twaha")
                .role(Role.ADMIN)
                .build();
        userRepository.save(abdulRazak);
    }
}
