package com.karan.mission_planning_system.security;

import com.karan.mission_planning_system.entity.User;
import com.karan.mission_planning_system.exception.UserNotFoundException;
import com.karan.mission_planning_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserRepository userRepository;

    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Unauthenticated access");
        }

        Object principal = authentication.getPrincipal();

        String username;

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else if (principal instanceof String str) {
            // fallback (rare cases)
            username = str;
        } else {
            throw new IllegalStateException("Invalid authentication principal");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found: " + username));
    }

    public void requireRole(String role) {
        User user = getCurrentUser();

        if (!user.getRole().name().equals(role)) {
            throw new SecurityException(
                    "Access denied: required role = " + role
            );
        }
    }
}
