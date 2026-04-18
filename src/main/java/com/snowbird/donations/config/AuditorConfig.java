package com.snowbird.donations.config;

import com.snowbird.donations.security.util.SecurityUtils;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditorConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            try {
                String currentUserId = SecurityUtils.getCurrentUserId();
                return Optional.ofNullable(currentUserId);
            } catch (Exception ex) {
                return Optional.of("SYSTEM");
            }
        };
    }
}
