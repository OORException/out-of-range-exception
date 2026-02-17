package br.edu.iff.ccc.webdev.security;

import br.edu.iff.ccc.webdev.exception.UnauthorizedException;
import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedException("No authenticated user found");
        }
        
        return authentication.getName();
    }

    public Long getCurrentUserId() {
        String email = getCurrentUserEmail();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found: " + email));
        
        return user.getId();
    }

    public User getCurrentUser() {
        String email = getCurrentUserEmail();
        
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found: " + email));
    }
}
