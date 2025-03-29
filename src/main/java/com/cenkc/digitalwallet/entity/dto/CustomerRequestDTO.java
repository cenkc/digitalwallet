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

    public @NotNull @Size(max = 50) String getName() {
        return this.name;
    }

    public @NotNull @Size(max = 50) String getSurname() {
        return this.surname;
    }

    public @NotNull String getTckn() {
        return this.tckn;
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CustomerRequestDTO)) return false;
        final CustomerRequestDTO other = (CustomerRequestDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$surname = this.getSurname();
        final Object other$surname = other.getSurname();
        if (this$surname == null ? other$surname != null : !this$surname.equals(other$surname)) return false;
        final Object this$tckn = this.getTckn();
        final Object other$tckn = other.getTckn();
        if (this$tckn == null ? other$tckn != null : !this$tckn.equals(other$tckn)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CustomerRequestDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $surname = this.getSurname();
        result = result * PRIME + ($surname == null ? 43 : $surname.hashCode());
        final Object $tckn = this.getTckn();
        result = result * PRIME + ($tckn == null ? 43 : $tckn.hashCode());
        return result;
    }

    public String toString() {
        return "CustomerRequestDTO(name=" + this.getName() + ", surname=" + this.getSurname() + ", tckn=" + this.getTckn() + ")";
    }
}
