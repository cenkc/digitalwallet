package com.cenkc.digitalwallet.entity.dto;

import com.cenkc.digitalwallet.entity.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletResponseDTO {

    private String walletName;
    private CurrencyType currencyType;
    private boolean activeForShopping;
    private boolean activeForWithdraw;
}
