package com.cenkc.digitalwallet.service.transaction;

import com.cenkc.digitalwallet.entity.TransactionStatusType;
import com.cenkc.digitalwallet.entity.dto.TransactionRequestDTO;
import com.cenkc.digitalwallet.entity.dto.TransactionResponseDTO;

import java.util.List;

public interface TransactionService {

    /**
     * Process a transaction (deposit or withdrawal)
     * @param requestDTO the transaction details
     * @return the created transaction
     */
    TransactionResponseDTO processTransaction(TransactionRequestDTO requestDTO);

    /**
     * Approve or deny a pending transaction
     * @param transactionId the id of the transaction
     * @param status the new status (APPROVED or DENIED)
     * @return the updated transaction
     */
    TransactionResponseDTO approveTransaction(Long transactionId, TransactionStatusType status);

    /**
     * Get all transactions for a specific wallet
     * @param walletId the wallet id
     * @return a list of transactions
     */
    List<TransactionResponseDTO> getTransactionsByWalletId(Long walletId);
}
