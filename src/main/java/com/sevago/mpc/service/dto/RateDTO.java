package com.sevago.mpc.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.sevago.mpc.domain.enumeration.RateCurrency;
import com.sevago.mpc.domain.enumeration.RateUnit;

/**
 * A DTO for the Rate entity.
 */
public class RateDTO implements Serializable {

    private Long id;

    private String description;

    private BigDecimal amount;

    @NotNull
    private RateCurrency currency;

    @NotNull
    private RateUnit unit;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public RateCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(RateCurrency currency) {
        this.currency = currency;
    }

    public RateUnit getUnit() {
        return unit;
    }

    public void setUnit(RateUnit unit) {
        this.unit = unit;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RateDTO rateDTO = (RateDTO) o;
        if(rateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RateDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", unit='" + getUnit() + "'" +
            "}";
    }
}
