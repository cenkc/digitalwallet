package com.cenkc.digitalwallet.entity;

import com.cenkc.digitalwallet.validation.TCKN;
import jakarta.persistence.*;
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

import java.util.Set;

@Entity
@Table(name = "customers")
@SQLDelete(sql = "UPDATE customers SET deleted = true WHERE id = ?")
@FilterDef(
        name = "deletedCustomerFilter",
        parameters = @ParamDef(name = "isDeleted", type = Boolean.class)
)
@Filter(
        name = "deletedCustomerFilter",
        condition = "deleted = :isDeleted"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @NotNull
    @OneToMany(mappedBy = "customer")
    private Set<Wallet> wallets;

    private boolean deleted = false;

}
