package com.snowbird.donations.donation.service.impl;

import com.snowbird.donations.common.exception.BadRequestException;
import com.snowbird.donations.common.exception.ResourceNotFoundException;
import com.snowbird.donations.donation.dto.request.CreateDonationCauseRequest;
import com.snowbird.donations.donation.dto.request.UpdateDonationCauseRequest;
import com.snowbird.donations.donation.dto.request.UpsertDonationCauseI18nRequest;
import com.snowbird.donations.donation.dto.response.DonationCauseResponse;
import com.snowbird.donations.donation.entity.DonationCause;
import com.snowbird.donations.donation.entity.DonationCauseI18n;
import com.snowbird.donations.donation.enums.CauseStatus;
import com.snowbird.donations.donation.repository.DonationCauseI18nRepository;
import com.snowbird.donations.donation.repository.DonationCauseRepository;
import com.snowbird.donations.donation.service.DonationCauseService;
import com.snowbird.donations.donation.service.helper.DonationI18nHelper;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DonationCauseServiceImpl implements DonationCauseService {

    private final DonationCauseRepository donationCauseRepository;
    private final DonationCauseI18nRepository donationCauseI18nRepository;
    private final DonationI18nHelper donationI18nHelper;

    @Override
    @Transactional(readOnly = true)
    public List<DonationCauseResponse> getActiveCauses(String language) {
        return donationCauseRepository.findByStatusOrderByDisplayOrderAscIdAsc(CauseStatus.ACTIVE)
                .stream()
                .map(cause -> mapToResponse(cause, language))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DonationCauseResponse getActiveCauseByCode(String code, String language) {
        DonationCause cause = donationCauseRepository.findByCode(code)
                .filter(c -> c.getStatus() == CauseStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active donation cause not found for code: " + code));

        return mapToResponse(cause, language);
    }

    @Override
    public DonationCauseResponse createCause(CreateDonationCauseRequest request) {
        validateAmountRange(request.getMinAmount(), request.getMaxAmount());
        validateActiveRange(request.getActiveFrom(), request.getActiveTo());

        String code = normalizeCode(request.getCode());
        if (donationCauseRepository.existsByCode(code)) {
            throw new BadRequestException("Donation cause code already exists: " + code);
        }

        DonationCause cause = new DonationCause();
        cause.setCode(code);
        cause.setDisplayOrder(request.getDisplayOrder());
        cause.setAllowCustomAmount(request.getAllowCustomAmount());
        cause.setMinAmount(request.getMinAmount());
        cause.setMaxAmount(request.getMaxAmount());
        cause.setCurrencyCode(request.getCurrencyCode().toUpperCase(Locale.ROOT));
        cause.setActiveFrom(request.getActiveFrom());
        cause.setActiveTo(request.getActiveTo());
        cause.setImageUrl(request.getImageUrl());
        cause.setStatus(CauseStatus.DRAFT);

        donationCauseRepository.save(cause);
        return mapToResponse(cause, null);
    }

    @Override
    public DonationCauseResponse updateCause(Long causeId, UpdateDonationCauseRequest request) {
        validateAmountRange(request.getMinAmount(), request.getMaxAmount());
        validateActiveRange(request.getActiveFrom(), request.getActiveTo());

        DonationCause cause = findCause(causeId);
        cause.setDisplayOrder(request.getDisplayOrder());
        cause.setAllowCustomAmount(request.getAllowCustomAmount());
        cause.setMinAmount(request.getMinAmount());
        cause.setMaxAmount(request.getMaxAmount());
        cause.setCurrencyCode(request.getCurrencyCode().toUpperCase(Locale.ROOT));
        cause.setActiveFrom(request.getActiveFrom());
        cause.setActiveTo(request.getActiveTo());
        cause.setImageUrl(request.getImageUrl());

        donationCauseRepository.save(cause);
        return mapToResponse(cause, null);
    }

    @Override
    public DonationCauseResponse changeCauseStatus(Long causeId, CauseStatus status) {
        DonationCause cause = findCause(causeId);

        if (status == CauseStatus.ACTIVE && cause.getI18nContents().isEmpty()) {
            throw new BadRequestException("Cannot activate cause without at least one i18n record");
        }

        cause.setStatus(status);
        donationCauseRepository.save(cause);
        return mapToResponse(cause, null);
    }

    @Override
    public DonationCauseResponse upsertCauseI18n(Long causeId, UpsertDonationCauseI18nRequest request) {
        DonationCause cause = findCause(causeId);
        String normalizedLanguageCode = request.getLanguageCode().trim().replace('_', '-');
        DonationCauseI18n i18n = donationCauseI18nRepository
                .findByCauseIdAndLanguageCode(causeId, normalizedLanguageCode)
                .orElseGet(() -> {
                    DonationCauseI18n fresh = new DonationCauseI18n();
                    fresh.setCause(cause);
                    fresh.setLanguageCode(normalizedLanguageCode);
                    return fresh;
                });

        i18n.setTitle(request.getTitle());
        i18n.setLanguageCode(normalizedLanguageCode);
        i18n.setShortDescription(request.getShortDescription());
        i18n.setFullDescription(request.getFullDescription());
        i18n.setCtaLabel(request.getCtaLabel());
        i18n.setThankYouMessage(request.getThankYouMessage());

        donationCauseI18nRepository.save(i18n);

        return mapToResponse(findCause(causeId), request.getLanguageCode());
    }

    @Override
    @Transactional(readOnly = true)
    public DonationCauseResponse getCauseById(Long causeId, String language) {
        return mapToResponse(findCause(causeId), language);
    }

    private DonationCause findCause(Long causeId) {
        return donationCauseRepository.findById(causeId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation cause not found for id: " + causeId));
    }

    private DonationCauseResponse mapToResponse(DonationCause cause, String language) {
        DonationCauseI18n translation = donationI18nHelper
                .resolve(cause.getI18nContents(), language)
                .orElse(null);

        return DonationCauseResponse.builder()
                .id(cause.getId())
                .code(cause.getCode())
                .status(cause.getStatus())
                .displayOrder(cause.getDisplayOrder())
                .allowCustomAmount(cause.getAllowCustomAmount())
                .minAmount(cause.getMinAmount())
                .maxAmount(cause.getMaxAmount())
                .currencyCode(cause.getCurrencyCode())
                .activeFrom(cause.getActiveFrom())
                .activeTo(cause.getActiveTo())
                .imageUrl(cause.getImageUrl())
                .languageCode(translation != null ? translation.getLanguageCode() : null)
                .title(translation != null ? translation.getTitle() : null)
                .shortDescription(translation != null ? translation.getShortDescription() : null)
                .fullDescription(translation != null ? translation.getFullDescription() : null)
                .ctaLabel(translation != null ? translation.getCtaLabel() : null)
                .thankYouMessage(translation != null ? translation.getThankYouMessage() : null)
                .build();
    }

    private void validateAmountRange(java.math.BigDecimal minAmount, java.math.BigDecimal maxAmount) {
        if (minAmount != null && maxAmount != null && maxAmount.compareTo(minAmount) < 0) {
            throw new BadRequestException("maxAmount must be greater than or equal to minAmount");
        }
    }

    private void validateActiveRange(java.time.OffsetDateTime activeFrom, java.time.OffsetDateTime activeTo) {
        if (activeFrom != null && activeTo != null && activeTo.isBefore(activeFrom)) {
            throw new BadRequestException("activeTo cannot be before activeFrom");
        }
    }

    private String normalizeCode(String code) {
        return code == null ? null : code.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
    }
}
