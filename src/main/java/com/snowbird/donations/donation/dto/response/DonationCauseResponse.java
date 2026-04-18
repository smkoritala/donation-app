package com.snowbird.donations.donation.dto.response;

import com.snowbird.donations.donation.enums.CauseStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DonationCauseResponse {

    private Long id;
    private String code;
    private CauseStatus status;
    private Integer displayOrder;
    private Boolean allowCustomAmount;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String currencyCode;
    private OffsetDateTime activeFrom;
    private OffsetDateTime activeTo;
    private String imageUrl;

    private String languageCode;
    private String title;
    private String shortDescription;
    private String fullDescription;
    private String ctaLabel;
    private String thankYouMessage;
}
