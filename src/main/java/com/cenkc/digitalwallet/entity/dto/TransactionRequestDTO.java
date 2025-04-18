package com.cenkc.digitalwallet.entity.dto;

import com.cenkc.digitalwallet.entity.OppositePartyType;
import com.cenkc.digitalwallet.entity.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

    @NotNull(message = "Wallet ID is required")
    private Long walletId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotNull(message = "Opposite party type is required")
    private OppositePartyType oppositePartyType;

    /**
     * Identifier for the opposite party - can be an IBAN number, payment reference ID,
     * or null if this is a transaction with another wallet in the system
     */
    private String oppositePartyIdentifier;

    /**
     * ID of another wallet in the system for internal transfers.
     * This should be null for external transactions (IBAN or PAYMENT)
     */
    private Long oppositePartyWalletId;

}
