package com.sevago.mpc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Preferences.
 */
@Entity
@Table(name = "preferences")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "preferences")
public class Preferences implements Serializable, MpcEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    private Instructor defaultInstructor;

    @ManyToOne
    private Activity defaultActivity;

    @ManyToOne
    private Location defaultLocation;

    @ManyToOne
    private LessonType defaultLessonType;

    @ManyToOne
    private Rate defaultRate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Preferences user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instructor getDefaultInstructor() {
        return defaultInstructor;
    }

    public Preferences defaultInstructor(Instructor instructor) {
        this.defaultInstructor = instructor;
        return this;
    }

    public void setDefaultInstructor(Instructor instructor) {
        this.defaultInstructor = instructor;
    }

    public Activity getDefaultActivity() {
        return defaultActivity;
    }

    public Preferences defaultActivity(Activity activity) {
        this.defaultActivity = activity;
        return this;
    }

    public void setDefaultActivity(Activity activity) {
        this.defaultActivity = activity;
    }

    public Location getDefaultLocation() {
        return defaultLocation;
    }

    public Preferences defaultLocation(Location location) {
        this.defaultLocation = location;
        return this;
    }

    public void setDefaultLocation(Location location) {
        this.defaultLocation = location;
    }

    public LessonType getDefaultLessonType() {
        return defaultLessonType;
    }

    public Preferences defaultLessonType(LessonType lessonType) {
        this.defaultLessonType = lessonType;
        return this;
    }

    public void setDefaultLessonType(LessonType lessonType) {
        this.defaultLessonType = lessonType;
    }

    public Rate getDefaultRate() {
        return defaultRate;
    }

    public Preferences defaultRate(Rate rate) {
        this.defaultRate = rate;
        return this;
    }

    public void setDefaultRate(Rate rate) {
        this.defaultRate = rate;
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
        Preferences preferences = (Preferences) o;
        if (preferences.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preferences.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Preferences{" +
            "id=" + getId() +
            "}";
    }
}
