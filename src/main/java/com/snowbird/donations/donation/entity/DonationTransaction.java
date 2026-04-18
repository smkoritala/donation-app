package com.snowbird.donations.donation.entity;

import com.snowbird.donations.common.entity.AuditableEntity;
import com.snowbird.donations.donation.enums.DonationStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "donation_transaction",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_donation_transaction_ref", columnNames = "transaction_ref")
    }
)
public class DonationTransaction extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_ref", nullable = false, length = 50)
    private String transactionRef;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cause_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_donation_transaction_cause"))
    private DonationCause cause;
    
    @Column(name = "donor_user_id", nullable = false, length = 100)
    private String donorUserId;

    @Column(name = "donor_display_name", length = 255)
    private String donorDisplayName;

    @Column(name = "donor_email", length = 255)
    private String donorEmail;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DonationStatus status = DonationStatus.INITIATED;

    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    @Column(name = "external_reference", length = 100)
    private String externalReference;

    @Column(name = "message_to_organization", length = 1000)
    private String messageToOrganization;

}
