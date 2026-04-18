package com.snowbird.donations.donation.entity;

import com.snowbird.donations.common.entity.AuditableEntity;
import com.snowbird.donations.donation.enums.CauseStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "donation_cause",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_donation_cause_code", columnNames = "code")
    }
)
public class DonationCause extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CauseStatus status = CauseStatus.DRAFT;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "allow_custom_amount", nullable = false)
    private Boolean allowCustomAmount = Boolean.TRUE;

    @Column(name = "min_amount", precision = 12, scale = 2)
    private BigDecimal minAmount;

    @Column(name = "max_amount", precision = 12, scale = 2)
    private BigDecimal maxAmount;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode = "USD";

    @Column(name = "active_from")
    private OffsetDateTime activeFrom;

    @Column(name = "active_to")
    private OffsetDateTime activeTo;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @OneToMany(mappedBy = "cause", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DonationCauseI18n> i18nContents = new ArrayList<>();
}
