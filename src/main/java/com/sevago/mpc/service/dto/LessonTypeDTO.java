package com.sevago.mpc.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the LessonType entity.
 */
public class LessonTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    @DecimalMax(value = "1")
    private BigDecimal ratio;

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

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
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

        LessonTypeDTO lessonTypeDTO = (LessonTypeDTO) o;
        if(lessonTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lessonTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LessonTypeDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", ratio=" + getRatio() +
            "}";
    }
}
