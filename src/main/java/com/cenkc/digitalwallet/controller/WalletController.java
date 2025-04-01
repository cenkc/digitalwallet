package com.cenkc.digitalwallet.controller;

import com.cenkc.digitalwallet.entity.dto.WalletRequestDTO;
import com.cenkc.digitalwallet.entity.dto.WalletResponseDTO;
import com.cenkc.digitalwallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<WalletResponseDTO> add(@RequestBody @Valid WalletRequestDTO walletRequestDTO) {
        return new ResponseEntity<>(walletService.addWallet(walletRequestDTO), HttpStatus.CREATED);
    }


    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<WalletResponseDTO> update(@PathVariable(name = "id") Long id,
                                                    @Valid @RequestBody WalletRequestDTO walletRequestDTO) {
        return new ResponseEntity<>(walletService.updateWallet(id, walletRequestDTO), HttpStatus.OK);
    }
}
