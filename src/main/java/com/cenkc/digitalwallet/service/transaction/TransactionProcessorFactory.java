package com.cenkc.digitalwallet.service.transaction;

import com.cenkc.digitalwallet.entity.TransactionType;
import org.springframework.stereotype.Component;

@Component
public class TransactionProcessorFactory {

    public TransactionProcessor getProcessor(TransactionType type) {
        switch (type) {
            case DEPOSIT:
                return new DepositTransactionProcessor();
            case WITHDRAW:
                return new WithdrawTransactionProcessor();
            default:
                throw new IllegalArgumentException("Unsupported transaction type: " + type);
        }
    }
}
