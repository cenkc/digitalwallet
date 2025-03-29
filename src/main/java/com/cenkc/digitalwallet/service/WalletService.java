package com.cenkc.digitalwallet.service;

import com.cenkc.digitalwallet.entity.dto.WalletRequestDTO;
import com.cenkc.digitalwallet.entity.dto.WalletResponseDTO;
import jakarta.validation.Valid;

public interface WalletService {
    WalletResponseDTO addWallet(@Valid WalletRequestDTO walletRequestDTO);
}
