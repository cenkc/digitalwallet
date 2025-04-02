package com.cenkc.digitalwallet.entity.dto;

import com.cenkc.digitalwallet.entity.OppositePartyType;
import com.cenkc.digitalwallet.entity.TransactionStatusType;
import com.cenkc.digitalwallet.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponseDTO {

    private Long id;
    private Long walletId;
    private BigDecimal amount;
    private TransactionType type;
    private OppositePartyType oppositePartyType;
    private TransactionStatusType status;
}
