package com.snowbird.donations.donation.service.impl;

import com.snowbird.donations.common.exception.BadRequestException;
import com.snowbird.donations.common.exception.ResourceNotFoundException;
import com.snowbird.donations.donation.dto.request.CreateDonationRequest;
import com.snowbird.donations.donation.dto.response.DonationCauseResponse;
import com.snowbird.donations.donation.dto.response.DonationTransactionResponse;
import com.snowbird.donations.donation.entity.DonationCause;
import com.snowbird.donations.donation.entity.DonationTransaction;
import com.snowbird.donations.donation.enums.CauseStatus;
import com.snowbird.donations.donation.enums.DonationStatus;
import com.snowbird.donations.donation.repository.DonationCauseRepository;
import com.snowbird.donations.donation.repository.DonationTransactionRepository;
import com.snowbird.donations.donation.service.DonationCauseService;
import com.snowbird.donations.donation.service.DonationTransactionService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DonationTransactionServiceImpl implements DonationTransactionService {

    private final DonationCauseRepository donationCauseRepository;
    private final DonationTransactionRepository donationTransactionRepository;
    private final DonationCauseService donationCauseService;

    @Override
    public DonationTransactionResponse createDonation(CreateDonationRequest request, String userId, String displayName, String email) {
        DonationCause cause = donationCauseRepository.findByCode(request.getCauseCode())
                .orElseThrow(() -> new ResourceNotFoundException("Donation cause not found for code: " + request.getCauseCode()));

        validateCauseActive(cause);
        validateAmount(cause, request);

        DonationTransaction tx = new DonationTransaction();
        tx.setTransactionRef(generateTransactionRef());
        tx.setCause(cause);
        tx.setDonorUserId(userId);
        tx.setDonorDisplayName(displayName);
        tx.setDonorEmail(email);
        tx.setAmount(request.getAmount());
        tx.setCurrencyCode(request.getCurrencyCode().toUpperCase(Locale.ROOT));
        tx.setMessageToOrganization(request.getMessageToOrganization());
        tx.setStatus(DonationStatus.PENDING);
        tx.setPaymentStatus("PENDING");

        donationTransactionRepository.save(tx);

        return mapToResponse(tx, null);//return mapToResponse(tx, cause.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationTransactionResponse> getMyDonations(String userId, String language) {
        return donationTransactionRepository.findByDonorUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(tx -> mapToResponse(tx, language))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DonationTransactionResponse getMyDonationByRef(String transactionRef, String userId, String language) {
        DonationTransaction tx = donationTransactionRepository.findByTransactionRefAndDonorUserId(transactionRef, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation transaction not found for ref: " + transactionRef));

        return mapToResponse(tx, language);
    }

    private void validateCauseActive(DonationCause cause) {
        if (cause.getStatus() != CauseStatus.ACTIVE) {
            throw new BadRequestException("Donation cause is not active");
        }

        OffsetDateTime now = OffsetDateTime.now();
        if (cause.getActiveFrom() != null && now.isBefore(cause.getActiveFrom())) {
            throw new BadRequestException("Donation cause is not yet active");
        }
        if (cause.getActiveTo() != null && now.isAfter(cause.getActiveTo())) {
            throw new BadRequestException("Donation cause is no longer active");
        }
    }

    private void validateAmount(DonationCause cause, CreateDonationRequest request) {
        if (!cause.getCurrencyCode().equalsIgnoreCase(request.getCurrencyCode())) {
            throw new BadRequestException("Currency does not match cause currency");
        }

        if (cause.getMinAmount() != null && request.getAmount().compareTo(cause.getMinAmount()) < 0) {
            throw new BadRequestException("Amount is below minimum allowed");
        }

        if (cause.getMaxAmount() != null && request.getAmount().compareTo(cause.getMaxAmount()) > 0) {
            throw new BadRequestException("Amount exceeds maximum allowed");
        }
    }

    private DonationTransactionResponse mapToResponse(DonationTransaction tx, String language) {
        DonationCauseResponse causeResponse = donationCauseService.getCauseById(tx.getCause().getId(), language);

        return DonationTransactionResponse.builder()
                .transactionRef(tx.getTransactionRef())
                .causeCode(tx.getCause().getCode())
                .causeTitle(causeResponse.getTitle())
                .amount(tx.getAmount())
                .currencyCode(tx.getCurrencyCode())
                .status(tx.getStatus())
                .paymentStatus(tx.getPaymentStatus())
                .paymentReference(tx.getPaymentReference())
                .externalReference(tx.getExternalReference())
                .messageToOrganization(tx.getMessageToOrganization())
                .createdAt(tx.getCreatedAt())
                .build();
    }

    private String generateTransactionRef() {
        return "DON-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase(Locale.ROOT);
    }
}
