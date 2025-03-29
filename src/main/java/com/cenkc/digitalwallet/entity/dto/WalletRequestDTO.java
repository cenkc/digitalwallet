package com.cenkc.digitalwallet.entity.dto;

import com.cenkc.digitalwallet.entity.CurrencyType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequestDTO {

    @NotNull
    @Size(max = 50)
    @Column(nullable = false)
    private String walletName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType currencyType;

    @Column(nullable = false)
    private boolean activeForShopping;

    @Column(nullable = false)
    private boolean activeForWithdraw;

}
