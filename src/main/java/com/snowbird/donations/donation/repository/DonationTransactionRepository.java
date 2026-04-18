package com.snowbird.donations.donation.repository;

import com.snowbird.donations.donation.entity.DonationTransaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationTransactionRepository extends JpaRepository<DonationTransaction, Long> {

    Optional<DonationTransaction> findByTransactionRef(String transactionRef);

    List<DonationTransaction> findByDonorUserIdOrderByCreatedAtDesc(String donorUserId);

    Optional<DonationTransaction> findByTransactionRefAndDonorUserId(String transactionRef, String donorUserId);
}
