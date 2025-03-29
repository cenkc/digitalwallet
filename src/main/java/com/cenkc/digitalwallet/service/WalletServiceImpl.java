package com.cenkc.digitalwallet.service;

import com.cenkc.digitalwallet.entity.Wallet;
import com.cenkc.digitalwallet.entity.dto.WalletRequestDTO;
import com.cenkc.digitalwallet.entity.dto.WalletResponseDTO;
import com.cenkc.digitalwallet.repository.WalletRepository;

public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletResponseDTO addWallet(WalletRequestDTO walletRequestDTO) {
        Wallet wallet = convertFromDTO(walletRequestDTO);
        Wallet savedWallet = walletRepository.save(wallet);
        return convertToDTO(savedWallet);
    }

    private Wallet convertFromDTO(WalletRequestDTO walletRequestDTO) {
        return Wallet.builder()
                .walletName(walletRequestDTO.getWalletName())
                .currencyType(walletRequestDTO.getCurrencyType())
                .activeForShopping(walletRequestDTO.isActiveForShopping())
                .activeForWithdraw(walletRequestDTO.isActiveForWithdraw())
                .build();
    }
    private WalletResponseDTO convertToDTO(Wallet wallet) {
        return WalletResponseDTO.builder()
                .walletName(wallet.getWalletName())
                .currencyType(wallet.getCurrencyType())
                .activeForShopping(wallet.isActiveForShopping())
                .activeForWithdraw(wallet.isActiveForWithdraw())
                .build();
    }
}
