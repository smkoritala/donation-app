package com.snowbird.donations.donation.controller;

import com.snowbird.donations.donation.dto.response.DonationCauseResponse;
import com.snowbird.donations.donation.service.DonationCauseService;
import com.snowbird.donations.donation.service.helper.DonationI18nHelper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/causes")
@RequiredArgsConstructor
public class DonationPublicController {

    private final DonationCauseService donationCauseService;
    private final DonationI18nHelper donationI18nHelper;

    @GetMapping
    public List<DonationCauseResponse> getActiveCauses(
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {

        String language = donationI18nHelper.resolveRequestLanguage(acceptLanguage);
        return donationCauseService.getActiveCauses(language);
    }

    @GetMapping("/{code}")
    public DonationCauseResponse getCauseByCode(
            @PathVariable String code,
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {

        String language = donationI18nHelper.resolveRequestLanguage(acceptLanguage);
        return donationCauseService.getActiveCauseByCode(code, language);
    }
}
