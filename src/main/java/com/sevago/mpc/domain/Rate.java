package com.sevago.mpc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.sevago.mpc.domain.enumeration.RateCurrency;

import com.sevago.mpc.domain.enumeration.RateUnit;


/**
 * A Rate.
 */
@Entity
@Table(name = "rate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rate")
public class Rate implements Serializable, MpcEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", precision=10, scale=2)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private RateCurrency currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private RateUnit unit;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "rates")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Instructor> instructors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Rate description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Rate amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public RateCurrency getCurrency() {
        return currency;
    }

    public Rate currency(RateCurrency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(RateCurrency currency) {
        this.currency = currency;
    }

    public RateUnit getUnit() {
        return unit;
    }

    public Rate unit(RateUnit unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(RateUnit unit) {
        this.unit = unit;
    }

    public User getUser() {
        return user;
    }

    public Rate user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Instructor> getInstructors() {
        return instructors;
    }

    public Rate instructors(Set<Instructor> instructors) {
        this.instructors = instructors;
        return this;
    }

    public Rate addInstructor(Instructor instructor) {
        this.instructors.add(instructor);
        instructor.getRates().add(this);
        return this;
    }

    public Rate removeInstructor(Instructor instructor) {
        this.instructors.remove(instructor);
        instructor.getRates().remove(this);
        return this;
    }

    public void setInstructors(Set<Instructor> instructors) {
        this.instructors = instructors;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rate rate = (Rate) o;
        if (rate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rate{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", unit='" + getUnit() + "'" +
            "}";
    }
}
