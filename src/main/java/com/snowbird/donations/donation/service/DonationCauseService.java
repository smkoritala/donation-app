package com.snowbird.donations.donation.service;

import com.snowbird.donations.donation.dto.request.CreateDonationCauseRequest;
import com.snowbird.donations.donation.dto.request.UpdateDonationCauseRequest;
import com.snowbird.donations.donation.dto.request.UpsertDonationCauseI18nRequest;
import com.snowbird.donations.donation.dto.response.DonationCauseResponse;
import com.snowbird.donations.donation.enums.CauseStatus;
import java.util.List;

public interface DonationCauseService {

    List<DonationCauseResponse> getActiveCauses(String language);

    DonationCauseResponse getActiveCauseByCode(String code, String language);

    DonationCauseResponse createCause(CreateDonationCauseRequest request);

    DonationCauseResponse updateCause(Long causeId, UpdateDonationCauseRequest request);

    DonationCauseResponse changeCauseStatus(Long causeId, CauseStatus status);

    DonationCauseResponse upsertCauseI18n(Long causeId, UpsertDonationCauseI18nRequest request);

    DonationCauseResponse getCauseById(Long causeId, String language);
}
