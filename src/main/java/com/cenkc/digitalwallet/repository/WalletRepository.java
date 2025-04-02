package com.cenkc.digitalwallet.repository;

import com.cenkc.digitalwallet.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    /**
     * Find a wallet by ID with a pessimistic write lock.
     * This ensures exclusive access to the wallet during transaction processing.
     *
     * @param id the wallet ID
     * @return the wallet with a pessimistic lock
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id = :id")
    Optional<Wallet> findByIdWithLock(@Param("id") Long id);

    /**
     * Find wallets by user ID
     *
     * @param userId the user ID
     * @return list of wallets belonging to the user
     */
    @Query("SELECT w FROM Wallet w WHERE w.user.id = :userId")
    Iterable<Wallet> findByUserId(@Param("userId") Long userId);
}
