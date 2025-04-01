package com.cenkc.digitalwallet.service;

import com.cenkc.digitalwallet.entity.User;
import com.cenkc.digitalwallet.entity.Wallet;
import com.cenkc.digitalwallet.entity.dto.WalletRequestDTO;
import com.cenkc.digitalwallet.entity.dto.WalletResponseDTO;
import com.cenkc.digitalwallet.exception.ResourceNotFoundException;
import com.cenkc.digitalwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    @Value("${wallet.default.balance:0.00}")
    private BigDecimal defaultBalance;

    @Value("${wallet.default.usableBalance:100.00}")
    private BigDecimal defaultUsableBalance;

    private final WalletRepository walletRepository;
    private final UserService userService;

    public WalletServiceImpl(WalletRepository walletRepository,
                             UserService userService) {
        this.walletRepository = walletRepository;
        this.userService = userService;
    }

    @Override
    public WalletResponseDTO addWallet(WalletRequestDTO walletRequestDTO) {
        // get current user
        User currentUser = userService.getCurrentUser();

        // Check if the wallet already exists
        Wallet wallet = convertFromDTO(walletRequestDTO);

        // Set the user for the wallet
        wallet.setUser(currentUser);

        // Set default values for balance and usableBalance
        wallet.setBalance(defaultBalance);
        wallet.setUsableBalance(defaultUsableBalance);

        // persist the wallet
        Wallet savedWallet = walletRepository.save(wallet);

        // Convert to DTO and return
        return convertToDTO(savedWallet);
    }

    @Override
    public WalletResponseDTO updateWallet(Long id, WalletRequestDTO walletRequestDTO) {
        // Check if the wallet exists
        Wallet existingWallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Wallet not found with id: %s", id)));

        // Update the wallet details
        existingWallet.setWalletName(walletRequestDTO.getWalletName());
        // TODO: is currencyType changeable?
        existingWallet.setCurrencyType(walletRequestDTO.getCurrencyType());
        existingWallet.setActiveForShopping(walletRequestDTO.isActiveForShopping());
        existingWallet.setActiveForWithdraw(walletRequestDTO.isActiveForWithdraw());

        // Save the updated wallet
        Wallet updatedWallet = walletRepository.save(existingWallet);

        // Convert to DTO and return
        return convertToDTO(updatedWallet);
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
