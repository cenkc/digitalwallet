package com.cenkc.digitalwallet.entity.dto;

import com.cenkc.digitalwallet.validation.TCKN;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {

    @NotNull
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String name;

    @NotNull
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String surname;

    @NotNull
    @TCKN
    @Column(nullable = false, unique = true, length = 11)
    private String tckn;
}
