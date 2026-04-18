package com.snowbird.donations.security.util;

import com.snowbird.donations.common.exception.UnauthorizedException;
import com.snowbird.donations.security.model.CurrentUser;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static CurrentUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CurrentUser currentUser)) {
            throw new UnauthorizedException("Invalid authenticated principal");
        }

        return currentUser;
    }

    public static String getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    public static Set<String> getCurrentRoles() {
        return getCurrentUser().getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toSet());
    }
}
