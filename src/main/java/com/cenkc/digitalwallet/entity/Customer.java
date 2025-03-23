package com.cenkc.digitalwallet.entity;

import com.cenkc.digitalwallet.validation.TCKN;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

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

    private boolean deleted = false;

    public Customer(Long id, @NotNull @Size(max = 50) String name, @NotNull @Size(max = 50) String surname, @NotNull String tckn, boolean deleted) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.tckn = tckn;
        this.deleted = deleted;
    }

    public Customer() {
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public @NotNull @Size(max = 50) String getName() {
        return this.name;
    }

    public @NotNull @Size(max = 50) String getSurname() {
        return this.surname;
    }

    public @NotNull String getTckn() {
        return this.tckn;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(@NotNull @Size(max = 50) String name) {
        this.name = name;
    }

    public void setSurname(@NotNull @Size(max = 50) String surname) {
        this.surname = surname;
    }

    public void setTckn(@NotNull String tckn) {
        this.tckn = tckn;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Customer)) return false;
        final Customer other = (Customer) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$surname = this.getSurname();
        final Object other$surname = other.getSurname();
        if (this$surname == null ? other$surname != null : !this$surname.equals(other$surname)) return false;
        final Object this$tckn = this.getTckn();
        final Object other$tckn = other.getTckn();
        if (this$tckn == null ? other$tckn != null : !this$tckn.equals(other$tckn)) return false;
        if (this.isDeleted() != other.isDeleted()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Customer;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $surname = this.getSurname();
        result = result * PRIME + ($surname == null ? 43 : $surname.hashCode());
        final Object $tckn = this.getTckn();
        result = result * PRIME + ($tckn == null ? 43 : $tckn.hashCode());
        result = result * PRIME + (this.isDeleted() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "Customer(id=" + this.getId() + ", name=" + this.getName() + ", surname=" + this.getSurname() + ", tckn=" + this.getTckn() + ", deleted=" + this.isDeleted() + ")";
    }

    public static class CustomerBuilder {
        private Long id;
        private @NotNull @Size(max = 50) String name;
        private @NotNull @Size(max = 50) String surname;
        private @NotNull String tckn;
        private boolean deleted;

        CustomerBuilder() {
        }

        public CustomerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder name(@NotNull @Size(max = 50) String name) {
            this.name = name;
            return this;
        }

        public CustomerBuilder surname(@NotNull @Size(max = 50) String surname) {
            this.surname = surname;
            return this;
        }

        public CustomerBuilder tckn(@NotNull String tckn) {
            this.tckn = tckn;
            return this;
        }

        public CustomerBuilder deleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Customer build() {
            return new Customer(id, name, surname, tckn, deleted);
        }

        public String toString() {
            return "Customer.CustomerBuilder(id=" + this.id + ", name=" + this.name + ", surname=" + this.surname + ", tckn=" + this.tckn + ", deleted=" + this.deleted + ")";
        }
    }
}
