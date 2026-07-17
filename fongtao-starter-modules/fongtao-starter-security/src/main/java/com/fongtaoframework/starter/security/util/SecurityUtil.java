package com.fongtaoframework.starter.security.util;

import com.fongtaoframework.starter.security.userdetails.LoginUserDetails;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static Optional<LoginUserDetails> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUserDetails loginUser) {
            return Optional.of(loginUser);
        }
        return Optional.empty();
    }
}
