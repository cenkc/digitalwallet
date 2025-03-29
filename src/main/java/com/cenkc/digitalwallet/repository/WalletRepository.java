package com.cenkc.digitalwallet.repository;

import com.cenkc.digitalwallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
