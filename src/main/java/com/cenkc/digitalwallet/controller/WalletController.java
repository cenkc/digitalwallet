package com.cenkc.digitalwallet.controller;

import com.cenkc.digitalwallet.entity.dto.WalletRequestDTO;
import com.cenkc.digitalwallet.entity.dto.WalletResponseDTO;
import com.cenkc.digitalwallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<WalletResponseDTO> addWallet(@RequestBody @Valid WalletRequestDTO walletRequestDTO) {
        return new ResponseEntity<>(walletService.addWallet(walletRequestDTO), HttpStatus.CREATED);
    }
}
