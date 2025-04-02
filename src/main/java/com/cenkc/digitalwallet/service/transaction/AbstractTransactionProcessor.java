package com.cenkc.digitalwallet.service.transaction;

import com.cenkc.digitalwallet.entity.Transaction;
import com.cenkc.digitalwallet.entity.TransactionStatusType;
import com.cenkc.digitalwallet.entity.Wallet;

/**
 * Abstract base implementation that provides template methods for common processing
 * Template Method Pattern: Defines the skeleton of the algorithm with steps that
 * can be customized by subclasses.
 */
public abstract class AbstractTransactionProcessor implements TransactionProcessor {

    // Template methods to be implemented by concrete classes
    protected abstract void updateWalletForApprovedTransaction(Transaction transaction, Wallet wallet);
    protected abstract void updateWalletForPendingTransaction(Transaction transaction, Wallet wallet);
    protected abstract void handlePendingToApproved(Transaction transaction, Wallet wallet);
    protected abstract void handlePendingToDenied(Transaction transaction, Wallet wallet);


    @Override
    public void process(Transaction transaction, Wallet wallet) {
        // Update wallet balance based on transaction status
        if (transaction.getStatus() == TransactionStatusType.APPROVED) {
            updateWalletForApprovedTransaction(transaction, wallet);
        } else if (transaction.getStatus() == TransactionStatusType.PENDING) {
            updateWalletForPendingTransaction(transaction, wallet);
        }
        // For DENIED status, we don't update the wallet
    }

    @Override
    public void processStatusChange(Transaction transaction, Wallet wallet, TransactionStatusType newStatus) {
        TransactionStatusType oldStatus = transaction.getStatus();

        // If status is changing from PENDING to APPROVED
        if (oldStatus == TransactionStatusType.PENDING && newStatus == TransactionStatusType.APPROVED) {
            handlePendingToApproved(transaction, wallet);
        }
        // If status is changing from PENDING to DENIED
        else if (oldStatus == TransactionStatusType.PENDING && newStatus == TransactionStatusType.DENIED) {
            handlePendingToDenied(transaction, wallet);
        }
        // Other status changes don't affect wallet balance
    }

}
