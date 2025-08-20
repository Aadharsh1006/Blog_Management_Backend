package com.examly.springapp.service;

import com.examly.springapp.model.Role;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only run this seeder if there are no users in the database
        if (userRepository.count() == 0) {
            // Create Admin User
            User admin = new User();
            admin.setName("Admin Hari");
            admin.setEmail("admin@hari.in");
            admin.setPassword(passwordEncoder.encode("adminhari"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            // Create a default Author User
            User author = new User();
            author.setName("Blog Author");
            author.setEmail("author@email.com");
            author.setPassword(passwordEncoder.encode("author"));
            author.setRole(Role.AUTHOR);
            userRepository.save(author);

            // Create a default Reader User
            User reader = new User();
            reader.setName("Blog Reader");
            reader.setEmail("user@email.com");
            reader.setPassword(passwordEncoder.encode("user"));
            reader.setRole(Role.READER);
            userRepository.save(reader);
        }
    }
}