package com.cenkc.digitalwallet.repository;

import com.cenkc.digitalwallet.entity.Transaction;
import com.cenkc.digitalwallet.entity.TransactionStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByWalletId(Long walletId);

    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId AND t.status = :status")
    List<Transaction> findByWalletIdAndStatus(@Param("walletId") Long walletId, @Param("status") TransactionStatusType status);

    @Query("SELECT t FROM Transaction t WHERE t.wallet.user.id = :userId")
    List<Transaction> findByUserId(@Param("userId") Long userId);
}
