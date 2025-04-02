package com.cenkc.digitalwallet.service.transaction;

import com.cenkc.digitalwallet.entity.Transaction;
import com.cenkc.digitalwallet.entity.TransactionStatusType;
import com.cenkc.digitalwallet.entity.Wallet;

/**
 * Interface defining transaction processing operations.
 * Strategy Pattern: Different implementations will handle transaction types differently.
 */
public interface TransactionProcessor {

    /**
     * Process a new transaction
     * @param transaction The transaction to process
     * @param wallet The wallet to update
     */
    void process(Transaction transaction, Wallet wallet);

    /**
     * Process a status change for an existing transaction
     * @param transaction The transaction being updated
     * @param wallet The wallet to update
     * @param newStatus The new status of the transaction
     */
    void processStatusChange(Transaction transaction, Wallet wallet, TransactionStatusType newStatus);
}