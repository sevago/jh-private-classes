package com.sevago.mpc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Lesson.
 */
@Entity
@Table(name = "lesson")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lesson")
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @NotNull
    @DecimalMax(value = "24")
    @Column(name = "duration", precision=10, scale=2, nullable = false)
    private BigDecimal duration;

    @NotNull
    @Column(name = "total_charge", precision=10, scale=2, nullable = false)
    private BigDecimal totalCharge;

    @ManyToOne(optional = false)
    @NotNull
    private Activity activity;

    @ManyToOne(optional = false)
    @NotNull
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    private LessonType lessonType;

    @ManyToOne(optional = false)
    @NotNull
    private Rate rate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "lesson_student",
               joinColumns = @JoinColumn(name="lessons_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="students_id", referencedColumnName="id"))
    private Set<Student> students = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Instructor teachingInstructor;

    @ManyToMany(mappedBy = "lessons")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Invoice> invoices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Lesson date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public Lesson duration(BigDecimal duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public BigDecimal getTotalCharge() {
        return totalCharge;
    }

    public Lesson totalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
        return this;
    }

    public void setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
    }

    public Activity getActivity() {
        return activity;
    }

    public Lesson activity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Location getLocation() {
        return location;
    }

    public Lesson location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public Lesson lessonType(LessonType lessonType) {
        this.lessonType = lessonType;
        return this;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public Rate getRate() {
        return rate;
    }

    public Lesson rate(Rate rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Lesson students(Set<Student> students) {
        this.students = students;
        return this;
    }

    public Lesson addStudent(Student student) {
        this.students.add(student);
        student.getLessons().add(this);
        return this;
    }

    public Lesson removeStudent(Student student) {
        this.students.remove(student);
        student.getLessons().remove(this);
        return this;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Instructor getTeachingInstructor() {
        return teachingInstructor;
    }

    public Lesson teachingInstructor(Instructor instructor) {
        this.teachingInstructor = instructor;
        return this;
    }

    public void setTeachingInstructor(Instructor instructor) {
        this.teachingInstructor = instructor;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public Lesson invoices(Set<Invoice> invoices) {
        this.invoices = invoices;
        return this;
    }

    public Lesson addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.getLessons().add(this);
        return this;
    }

    public Lesson removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.getLessons().remove(this);
        return this;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
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
        Lesson lesson = (Lesson) o;
        if (lesson.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lesson.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Lesson{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", duration=" + getDuration() +
            ", totalCharge=" + getTotalCharge() +
            "}";
    }
}
