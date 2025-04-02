package com.cenkc.digitalwallet.entity.dto;

import com.cenkc.digitalwallet.entity.TransactionStatusType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionApprovalDTO {

    @NotNull(message = "Transaction status is required")
    private TransactionStatusType status;
}
