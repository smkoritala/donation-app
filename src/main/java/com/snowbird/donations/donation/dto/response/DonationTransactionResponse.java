package com.snowbird.donations.donation.dto.response;

import com.snowbird.donations.donation.enums.DonationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DonationTransactionResponse {

    private String transactionRef;
    private String causeCode;
    private String causeTitle;
    private BigDecimal amount;
    private String currencyCode;
    private DonationStatus status;
    private String paymentStatus;
    private String paymentReference;
    private String externalReference;
    private String messageToOrganization;
    private LocalDateTime createdAt;
}
