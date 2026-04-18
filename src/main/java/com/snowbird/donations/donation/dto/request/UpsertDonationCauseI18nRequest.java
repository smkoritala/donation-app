package com.snowbird.donations.donation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpsertDonationCauseI18nRequest {

    @NotBlank
    private String languageCode;

    @NotBlank
    private String title;

    private String shortDescription;
    private String fullDescription;
    private String ctaLabel;
    private String thankYouMessage;
}
