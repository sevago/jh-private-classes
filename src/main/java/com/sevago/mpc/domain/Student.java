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
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 70)
    @Pattern(regexp = "^[ A-Za-z0-9]+$")
    @Column(name = "name", length = 70, nullable = false)
    private String name;

    @Size(min = 10, max = 13)
    @Column(name = "phone_number", length = 13)
    private String phoneNumber;

    @Size(min = 7, max = 100)
    @Pattern(regexp = "^[_.@A-Za-z0-9-]+$")
    @Column(name = "email", length = 100)
    private String email;

    @OneToMany(mappedBy = "billToStudent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Invoice> invoices = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lesson> lessons = new HashSet<>();

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

    public Student name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Student phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Student email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public Student invoices(Set<Invoice> invoices) {
        this.invoices = invoices;
        return this;
    }

    public Student addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setBillToStudent(this);
        return this;
    }

    public Student removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setBillToStudent(null);
        return this;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public User getUser() {
        return user;
    }

    public Student user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public Student lessons(Set<Lesson> lessons) {
        this.lessons = lessons;
        return this;
    }

    public Student addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.getStudents().add(this);
        return this;
    }

    public Student removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.getStudents().remove(this);
        return this;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
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
        Student student = (Student) o;
        if (student.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
