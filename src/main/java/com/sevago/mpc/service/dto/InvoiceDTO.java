package com.sevago.mpc.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.sevago.mpc.domain.enumeration.RateCurrency;

/**
 * A DTO for the Invoice entity.
 */
public class InvoiceDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer number;

    @NotNull
    private LocalDate periodStartDate;

    @NotNull
    private LocalDate periodEndDate;

    @NotNull
    private LocalDate issueDate;

    @NotNull
    private LocalDate dueDate;

    private BigDecimal totalAmount;

    private RateCurrency totalCurrency;

    private Set<LessonDTO> lessons = new HashSet<>();

    private Long billToStudentId;

    private String billToStudentName;

    private Long teachingInstructorId;

    private String teachingInstructorName;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public RateCurrency getTotalCurrency() {
        return totalCurrency;
    }

    public void setTotalCurrency(RateCurrency totalCurrency) {
        this.totalCurrency = totalCurrency;
    }

    public Set<LessonDTO> getLessons() {
        return lessons;
    }

    public void setLessons(Set<LessonDTO> lessons) {
        this.lessons = lessons;
    }

    public Long getBillToStudentId() {
        return billToStudentId;
    }

    public void setBillToStudentId(Long studentId) {
        this.billToStudentId = studentId;
    }

    public String getBillToStudentName() {
        return billToStudentName;
    }

    public void setBillToStudentName(String studentName) {
        this.billToStudentName = studentName;
    }

    public Long getTeachingInstructorId() {
        return teachingInstructorId;
    }

    public void setTeachingInstructorId(Long instructorId) {
        this.teachingInstructorId = instructorId;
    }

    public String getTeachingInstructorName() {
        return teachingInstructorName;
    }

    public void setTeachingInstructorName(String instructorName) {
        this.teachingInstructorName = instructorName;
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if(invoiceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
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
