package com.sevago.mpc.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Lesson entity.
 */
public class LessonDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    @DecimalMax(value = "24")
    private BigDecimal duration;

    @NotNull
    private BigDecimal totalCharge;

    private Long activityId;

    private String activityName;

    private Long locationId;

    private String locationName;

    private Long lessonTypeId;

    private String lessonTypeDescription;

    private Long rateId;

    private String rateDescription;

    private Set<StudentDTO> students = new HashSet<>();

    private Long teachingInstructorId;

    private String teachingInstructorName;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public BigDecimal getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getLessonTypeId() {
        return lessonTypeId;
    }

    public void setLessonTypeId(Long lessonTypeId) {
        this.lessonTypeId = lessonTypeId;
    }

    public String getLessonTypeDescription() {
        return lessonTypeDescription;
    }

    public void setLessonTypeDescription(String lessonTypeDescription) {
        this.lessonTypeDescription = lessonTypeDescription;
    }

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    public String getRateDescription() {
        return rateDescription;
    }

    public void setRateDescription(String rateDescription) {
        this.rateDescription = rateDescription;
    }

    public Set<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentDTO> students) {
        this.students = students;
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

        LessonDTO lessonDTO = (LessonDTO) o;
        if(lessonDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lessonDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LessonDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", duration=" + getDuration() +
            ", totalCharge=" + getTotalCharge() +
            "}";
    }
}
