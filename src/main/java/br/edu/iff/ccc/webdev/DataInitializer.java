package br.edu.iff.ccc.webdev;

import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.model.enums.UserLevel;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner seedAdmin() {
        return args -> {
            if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .fullName("Administrador")
                        .email("admin@admin.com")
                        .level(UserLevel.ADMIN)
                        .createdAt(Instant.now())
                        .build();

                admin.changePasswordHash(passwordEncoder.encode("admin123"));

                userRepository.save(admin);
                System.out.println(">>> Admin criado com sucesso.");
            }
        };
    }
}