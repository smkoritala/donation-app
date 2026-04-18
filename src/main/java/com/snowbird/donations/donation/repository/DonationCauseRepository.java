package com.snowbird.donations.donation.repository;

import com.snowbird.donations.donation.entity.DonationCause;
import com.snowbird.donations.donation.enums.CauseStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationCauseRepository extends JpaRepository<DonationCause, Long> {

    Optional<DonationCause> findByCode(String code);

    boolean existsByCode(String code);

    List<DonationCause> findByStatusOrderByDisplayOrderAscIdAsc(CauseStatus status);
}
