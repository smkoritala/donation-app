package com.snowbird.donations.donation.service;

import com.snowbird.donations.donation.dto.request.CreateDonationRequest;
import com.snowbird.donations.donation.dto.response.DonationTransactionResponse;
import java.util.List;

public interface DonationTransactionService {

    DonationTransactionResponse createDonation(CreateDonationRequest request, String userId, String displayName, String email);

    List<DonationTransactionResponse> getMyDonations(String userId, String language);

    DonationTransactionResponse getMyDonationByRef(String transactionRef, String userId, String language);
}
