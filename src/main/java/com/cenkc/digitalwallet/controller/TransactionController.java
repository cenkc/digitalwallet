package com.cenkc.digitalwallet.controller;

import com.cenkc.digitalwallet.entity.dto.TransactionApprovalDTO;
import com.cenkc.digitalwallet.entity.dto.TransactionRequestDTO;
import com.cenkc.digitalwallet.entity.dto.TransactionResponseDTO;
import com.cenkc.digitalwallet.service.transaction.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping({"", "/"})
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<TransactionResponseDTO> processTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return new ResponseEntity<>(transactionService.processTransaction(transactionRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/wallet/{walletId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByWallet(@PathVariable Long walletId) {
        return new ResponseEntity<>(transactionService.getTransactionsByWalletId(walletId), HttpStatus.OK);
    }

    @PutMapping("/{transactionId}/approve")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TransactionResponseDTO> approveOrDenyTransaction(
            @PathVariable Long transactionId,
            @Valid @RequestBody TransactionApprovalDTO approvalDTO) {
        return new ResponseEntity<>(
                transactionService.approveTransaction(transactionId, approvalDTO.getStatus()),
                HttpStatus.OK);
    }
}
