package com.snowbird.donations.donation.service.helper;

import com.snowbird.donations.donation.entity.DonationCauseI18n;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DonationI18nHelper {

    @Value("${app.i18n.fallback-language:en}")
    private String fallbackLanguage;

    public Optional<DonationCauseI18n> resolve(List<DonationCauseI18n> translations, String requestedLanguage) {
        if (translations == null || translations.isEmpty()) {
            return Optional.empty();
        }

        String normalizedRequested = normalize(requestedLanguage);
        String requestedBase = extractBaseLanguage(normalizedRequested);
        String fallback = normalize(fallbackLanguage);

        return translations.stream()
                .filter(t -> normalize(t.getLanguageCode()).equalsIgnoreCase(normalizedRequested))
                .findFirst()
                .or(() -> translations.stream()
                        .filter(t -> extractBaseLanguage(normalize(t.getLanguageCode()))
                                .equalsIgnoreCase(requestedBase))
                        .findFirst())
                .or(() -> translations.stream()
                        .filter(t -> normalize(t.getLanguageCode()).equalsIgnoreCase(fallback))
                        .findFirst())
                .or(() -> translations.stream().findFirst());
    }

    public String resolveRequestLanguage(String acceptLanguageHeader) {
        if (acceptLanguageHeader == null || acceptLanguageHeader.isBlank()) {
            return fallbackLanguage;
        }

        try {
            return Locale.forLanguageTag(acceptLanguageHeader).toLanguageTag();
        } catch (Exception ex) {
            return fallbackLanguage;
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().replace('_', '-');
    }

    private String extractBaseLanguage(String languageTag) {
        if (languageTag == null || languageTag.isBlank()) {
            return "";
        }
        int idx = languageTag.indexOf('-');
        return idx > 0 ? languageTag.substring(0, idx) : languageTag;
    }
}
