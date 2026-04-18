package com.snowbird.donations.donation.controller;

import com.snowbird.donations.donation.dto.request.CreateDonationCauseRequest;
import com.snowbird.donations.donation.dto.request.UpdateDonationCauseRequest;
import com.snowbird.donations.donation.dto.request.UpsertDonationCauseI18nRequest;
import com.snowbird.donations.donation.dto.response.DonationCauseResponse;
import com.snowbird.donations.donation.enums.CauseStatus;
import com.snowbird.donations.donation.service.DonationCauseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/causes")
@RequiredArgsConstructor
public class DonationAdminController {

    private final DonationCauseService donationCauseService;

    @PostMapping
    public DonationCauseResponse createCause(@Valid @RequestBody CreateDonationCauseRequest request) {
        return donationCauseService.createCause(request);
    }

    @PutMapping("/{causeId}")
    public DonationCauseResponse updateCause(
            @PathVariable Long causeId,
            @Valid @RequestBody UpdateDonationCauseRequest request) {

        return donationCauseService.updateCause(causeId, request);
    }

    @PutMapping("/{causeId}/status")
    public DonationCauseResponse changeStatus(
            @PathVariable Long causeId,
            @RequestParam CauseStatus status) {

        return donationCauseService.changeCauseStatus(causeId, status);
    }

    @PostMapping("/{causeId}/i18n")
    public DonationCauseResponse upsertI18n(
            @PathVariable Long causeId,
            @Valid @RequestBody UpsertDonationCauseI18nRequest request) {

        return donationCauseService.upsertCauseI18n(causeId, request);
    }

    @GetMapping("/{causeId}")
    public DonationCauseResponse getCauseById(
            @PathVariable Long causeId,
            @RequestParam(required = false) String lang) {

        return donationCauseService.getCauseById(causeId, lang);
    }
}
