package com.sevago.mpc.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Instructor entity.
 */
public class InstructorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 70)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String name;

    @Size(min = 1, max = 255)
    private String address;

    @Size(min = 10, max = 13)
    private String phoneNumber;

    @Size(min = 7, max = 100)
    @Pattern(regexp = "^[_.@A-Za-z0-9-]+$")
    private String email;

    private Long userId;

    private String userLogin;

    private Set<RateDTO> rates = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Set<RateDTO> getRates() {
        return rates;
    }

    public void setRates(Set<RateDTO> rates) {
        this.rates = rates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InstructorDTO instructorDTO = (InstructorDTO) o;
        if(instructorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), instructorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InstructorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
