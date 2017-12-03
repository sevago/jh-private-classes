package com.sevago.mpc.domain;

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

import com.sevago.mpc.domain.enumeration.RateCurrency;


/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_number", nullable = false)
    private Integer number;

    @NotNull
    @Column(name = "period_start_date", nullable = false)
    private LocalDate periodStartDate;

    @NotNull
    @Column(name = "period_end_date", nullable = false)
    private LocalDate periodEndDate;

    @NotNull
    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "total_amount", precision=10, scale=2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "total_currency")
    private RateCurrency totalCurrency;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "invoice_lesson",
               joinColumns = @JoinColumn(name="invoices_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="lessons_id", referencedColumnName="id"))
    private Set<Lesson> lessons = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Student billToStudent;

    @ManyToOne(optional = false)
    @NotNull
    private Instructor teachingInstructor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public Invoice number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public Invoice periodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
        return this;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public Invoice periodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
        return this;
    }

    public void setPeriodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public Invoice issueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
        return this;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Invoice dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Invoice totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public RateCurrency getTotalCurrency() {
        return totalCurrency;
    }

    public Invoice totalCurrency(RateCurrency totalCurrency) {
        this.totalCurrency = totalCurrency;
        return this;
    }

    public void setTotalCurrency(RateCurrency totalCurrency) {
        this.totalCurrency = totalCurrency;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public Invoice lessons(Set<Lesson> lessons) {
        this.lessons = lessons;
        return this;
    }

    public Invoice addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.getInvoices().add(this);
        return this;
    }

    public Invoice removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.getInvoices().remove(this);
        return this;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Student getBillToStudent() {
        return billToStudent;
    }

    public Invoice billToStudent(Student student) {
        this.billToStudent = student;
        return this;
    }

    public void setBillToStudent(Student student) {
        this.billToStudent = student;
    }

    public Instructor getTeachingInstructor() {
        return teachingInstructor;
    }

    public Invoice teachingInstructor(Instructor instructor) {
        this.teachingInstructor = instructor;
        return this;
    }

    public void setTeachingInstructor(Instructor instructor) {
        this.teachingInstructor = instructor;
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
        Invoice invoice = (Invoice) o;
        if (invoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", periodStartDate='" + getPeriodStartDate() + "'" +
            ", periodEndDate='" + getPeriodEndDate() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", totalCurrency='" + getTotalCurrency() + "'" +
            "}";
    }
}
