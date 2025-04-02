package com.cenkc.digitalwallet.service.transaction;

import com.cenkc.digitalwallet.entity.Transaction;
import com.cenkc.digitalwallet.entity.Wallet;

public class WithdrawTransactionProcessor extends AbstractTransactionProcessor {

    @Override
    protected void updateWalletForApprovedTransaction(Transaction transaction, Wallet wallet) {
        // For approved withdrawals, update both balance and usable balance
        wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
        wallet.setUsableBalance(wallet.getUsableBalance().subtract(transaction.getAmount()));
    }

    @Override
    protected void updateWalletForPendingTransaction(Transaction transaction, Wallet wallet) {
        // For pending withdrawals, update only usable balance to reserve funds
        wallet.setUsableBalance(wallet.getUsableBalance().subtract(transaction.getAmount()));
    }

    @Override
    protected void handlePendingToApproved(Transaction transaction, Wallet wallet) {
        // When a pending withdrawal becomes approved, update balance
        // Usable balance was already updated when it was pending
        wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
    }

    @Override
    protected void handlePendingToDenied(Transaction transaction, Wallet wallet) {
        // When a pending withdrawal is denied, restore the usable balance
        wallet.setUsableBalance(wallet.getUsableBalance().add(transaction.getAmount()));
    }
}
