package com.snowbird.donations.donation.controller;

import com.snowbird.donations.donation.dto.request.CreateDonationRequest;
import com.snowbird.donations.donation.dto.response.DonationTransactionResponse;
import com.snowbird.donations.donation.service.DonationTransactionService;
import com.snowbird.donations.donation.service.helper.DonationI18nHelper;
import com.snowbird.donations.security.model.CurrentUser;
import com.snowbird.donations.security.util.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationTransactionService donationTransactionService;
    private final DonationI18nHelper donationI18nHelper;

    @PostMapping
    public DonationTransactionResponse createDonation(@Valid @RequestBody CreateDonationRequest request) {
        CurrentUser currentUser = SecurityUtils.getCurrentUser();

        return donationTransactionService.createDonation(
                request,
                currentUser.getUserId(),
                currentUser.getName(),
                currentUser.getEmail()
        );
    }

    @GetMapping("/me")
    public List<DonationTransactionResponse> getMyDonations(
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {

        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        String language = donationI18nHelper.resolveRequestLanguage(acceptLanguage);

        return donationTransactionService.getMyDonations(currentUser.getUserId(), language);
    }

    @GetMapping("/me/{transactionRef}")
    public DonationTransactionResponse getMyDonationByRef(
            @PathVariable String transactionRef,
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {

        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        String language = donationI18nHelper.resolveRequestLanguage(acceptLanguage);

        return donationTransactionService.getMyDonationByRef(transactionRef, currentUser.getUserId(), language);
    }
}
