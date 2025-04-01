package com.cenkc.digitalwallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "wallets")
@SQLDelete(sql = "UPDATE wallets SET deleted = true WHERE id = ?")
@FilterDef(
        name = "deletedWalletFilter",
        parameters = @ParamDef(name = "isDeleted", type = Boolean.class)
)
@Filter(
        name = "deletedWalletFilter",
        condition = "deleted = :isDeleted"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {

    // TODO should use UUID in order to prevent
    //  accessing Wallet data using consecutive numbers
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallets_generator")
    @SequenceGenerator(name = "wallets_generator", sequenceName = "wallets_seq", allocationSize = 1, initialValue = 1000)
    private Long id;

    // Many wallets can belong to one user
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Size(max = 50)
    @Column(nullable = false, unique = true)
    private String walletName;

    @NotNull
    @OneToMany(mappedBy = "wallet")
    Set<Transaction> transactions;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType currencyType;

    @Column(nullable = false)
    private boolean activeForShopping;

    @Column(nullable = false)
    private boolean activeForWithdraw;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private BigDecimal balance;

    // TODO make sure if it's kind of a "arti para"?
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private BigDecimal usableBalance;

    private boolean deleted = false;
}
