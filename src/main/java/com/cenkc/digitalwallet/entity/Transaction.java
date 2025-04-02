package com.cenkc.digitalwallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;

@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(name = "idx_transactions_wallet_id", columnList = "wallet_id"),
                @Index(name = "idx_transactions_status", columnList = "status"),
                @Index(name = "idx_transactions_opposite_party_wallet_id", columnList = "opposite_party_wallet_id")
        }
)
@SQLDelete(sql = "UPDATE transactions SET deleted = true WHERE id = ?")
@FilterDef(
        name = "deletedTransactionFilter",
        parameters = @ParamDef(name = "isDeleted", type = Boolean.class)
)
@Filter(
        name = "deletedTransactionFilter",
        condition = "deleted = :isDeleted"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_generator")
    @SequenceGenerator(name = "transactions_generator", sequenceName = "transactions_seq", allocationSize = 1, initialValue = 1000)
    private Long id;

    // Many transactions can belong to one wallet
    @NotNull
    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OppositePartyType oppositePartyType;

    /**
     * The identifier of the opposite party:
     * - For IBAN: The bank account number (e.g., "TR330006100519786457841326")
     * - For PAYMENT: The payment reference ID (e.g., "PAY123456789")
     */
    // TODO Implement a custom @IBAN validator (refer to @TCKN)
    @Column(name = "opposite_party_identifier")
    private String oppositePartyIdentifier;

    /**
     * For internal transfers between wallets within the system.
     * This is only populated when transferring between two wallets in the system.
     * For external transactions (bank transfers, payments), this will be null.
     */
    @ManyToOne
    @JoinColumn(name = "opposite_party_wallet_id")
    private Wallet oppositePartyWallet;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatusType status;

    private boolean deleted = false;
}
