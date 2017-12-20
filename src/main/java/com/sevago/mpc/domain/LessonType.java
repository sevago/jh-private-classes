package com.sevago.mpc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * A LessonType.
 */
@Entity
@Table(name = "lesson_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lessontype")
public class LessonType implements Serializable, MpcEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @DecimalMax(value = "1")
    @Column(name = "ratio", precision=10, scale=2, nullable = false)
    private BigDecimal ratio;

    @ManyToOne
    private User user;

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

    public LessonType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public LessonType ratio(BigDecimal ratio) {
        this.ratio = ratio;
        return this;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public User getUser() {
        return user;
    }

    public LessonType user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        LessonType lessonType = (LessonType) o;
        if (lessonType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lessonType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LessonType{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", ratio=" + getRatio() +
            "}";
    }
}
