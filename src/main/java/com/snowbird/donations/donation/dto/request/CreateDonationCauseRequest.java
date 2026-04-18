package com.snowbird.donations.donation.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDonationCauseRequest {

    @NotBlank
    private String code;

    private Integer displayOrder;

    @NotNull
    private Boolean allowCustomAmount;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal minAmount;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal maxAmount;

    @NotBlank
    private String currencyCode;

    private OffsetDateTime activeFrom;
    private OffsetDateTime activeTo;

    private String imageUrl;
}
