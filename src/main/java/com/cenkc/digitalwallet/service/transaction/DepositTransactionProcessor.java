package com.cenkc.digitalwallet.service.transaction;

import com.cenkc.digitalwallet.entity.Transaction;
import com.cenkc.digitalwallet.entity.Wallet;

public class DepositTransactionProcessor extends AbstractTransactionProcessor {
    /**
     * @param transaction
     * @param wallet
     */
    @Override
    protected void updateWalletForApprovedTransaction(Transaction transaction, Wallet wallet) {
        // For approved deposits, update both balance and usable balance
        wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
        wallet.setUsableBalance(wallet.getUsableBalance().add(transaction.getAmount()));
    }

    /**
     * @param transaction
     * @param wallet
     */
    @Override
    protected void updateWalletForPendingTransaction(Transaction transaction, Wallet wallet) {
        // For pending deposits, update only balance
        wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
    }

    /**
     * @param transaction
     * @param wallet
     */
    @Override
    protected void handlePendingToApproved(Transaction transaction, Wallet wallet) {
        // When a pending deposit becomes approved, update usable balance
        // Balance was already updated when it was pending
        wallet.setUsableBalance(wallet.getUsableBalance().add(transaction.getAmount()));
    }

    /**
     * @param transaction
     * @param wallet
     */
    @Override
    protected void handlePendingToDenied(Transaction transaction, Wallet wallet) {
        throw new IllegalStateException("Implement this");
    }
}
