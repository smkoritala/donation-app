package com.snowbird.donations.donation.repository;

import com.snowbird.donations.donation.entity.DonationCauseI18n;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationCauseI18nRepository extends JpaRepository<DonationCauseI18n, Long> {

    Optional<DonationCauseI18n> findByCauseIdAndLanguageCode(Long causeId, String languageCode);

    List<DonationCauseI18n> findByCauseId(Long causeId);

    boolean existsByCauseIdAndLanguageCode(Long causeId, String languageCode);
}
