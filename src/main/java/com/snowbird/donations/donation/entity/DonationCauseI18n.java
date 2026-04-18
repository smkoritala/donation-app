package com.snowbird.donations.donation.entity;

import com.snowbird.donations.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "donation_cause_i18n",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_donation_cause_i18n_cause_lang",
            columnNames = {"cause_id", "language_code"}
        )
    }
)
public class DonationCauseI18n extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cause_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_donation_cause_i18n_cause"))
    private DonationCause cause;

    @Column(name = "language_code", nullable = false, length = 10)
    private String languageCode;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "short_description", length = 1000)
    private String shortDescription;

    @Column(name = "full_description", columnDefinition = "TEXT")
    private String fullDescription;

    @Column(name = "cta_label", length = 100)
    private String ctaLabel;

    @Column(name = "thank_you_message", columnDefinition = "TEXT")
    private String thankYouMessage;
}
