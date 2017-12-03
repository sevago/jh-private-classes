package com.sevago.mpc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Instructor.
 */
@Entity
@Table(name = "instructor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instructor")
public class Instructor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 70)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    @Column(name = "name", length = 70, nullable = false)
    private String name;

    @Size(min = 1, max = 255)
    @Column(name = "address", length = 255)
    private String address;

    @Size(min = 10, max = 13)
    @Column(name = "phone_number", length = 13)
    private String phoneNumber;

    @Size(min = 7, max = 100)
    @Pattern(regexp = "^[_.@A-Za-z0-9-]+$")
    @Column(name = "email", length = 100)
    private String email;

    @OneToMany(mappedBy = "teachingInstructor")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Invoice> invoices = new HashSet<>();

    @OneToMany(mappedBy = "teachingInstructor")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lesson> lessons = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "instructor_rate",
               joinColumns = @JoinColumn(name="instructors_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="rates_id", referencedColumnName="id"))
    private Set<Rate> rates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Instructor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public Instructor address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Instructor phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Instructor email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public Instructor invoices(Set<Invoice> invoices) {
        this.invoices = invoices;
        return this;
    }

    public Instructor addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setTeachingInstructor(this);
        return this;
    }

    public Instructor removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setTeachingInstructor(null);
        return this;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public Instructor lessons(Set<Lesson> lessons) {
        this.lessons = lessons;
        return this;
    }

    public Instructor addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.setTeachingInstructor(this);
        return this;
    }

    public Instructor removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.setTeachingInstructor(null);
        return this;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    public User getUser() {
        return user;
    }

    public Instructor user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Rate> getRates() {
        return rates;
    }

    public Instructor rates(Set<Rate> rates) {
        this.rates = rates;
        return this;
    }

    public Instructor addRate(Rate rate) {
        this.rates.add(rate);
        rate.getInstructors().add(this);
        return this;
    }

    public Instructor removeRate(Rate rate) {
        this.rates.remove(rate);
        rate.getInstructors().remove(this);
        return this;
    }

    public void setRates(Set<Rate> rates) {
        this.rates = rates;
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
        Instructor instructor = (Instructor) o;
        if (instructor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), instructor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Instructor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
