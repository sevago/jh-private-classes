package com.sevago.mpc.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Preferences entity.
 */
public class PreferencesDTO implements Serializable {

    private Long id;

    private Long userId;

    private String userLogin;

    private Long defaultInstructorId;

    private String defaultInstructorName;

    private Long defaultActivityId;

    private String defaultActivityName;

    private Long defaultLocationId;

    private String defaultLocationName;

    private Long defaultLessonTypeId;

    private String defaultLessonTypeDescription;

    private Long defaultRateId;

    private String defaultRateDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getDefaultInstructorId() {
        return defaultInstructorId;
    }

    public void setDefaultInstructorId(Long instructorId) {
        this.defaultInstructorId = instructorId;
    }

    public String getDefaultInstructorName() {
        return defaultInstructorName;
    }

    public void setDefaultInstructorName(String instructorName) {
        this.defaultInstructorName = instructorName;
    }

    public Long getDefaultActivityId() {
        return defaultActivityId;
    }

    public void setDefaultActivityId(Long activityId) {
        this.defaultActivityId = activityId;
    }

    public String getDefaultActivityName() {
        return defaultActivityName;
    }

    public void setDefaultActivityName(String activityName) {
        this.defaultActivityName = activityName;
    }

    public Long getDefaultLocationId() {
        return defaultLocationId;
    }

    public void setDefaultLocationId(Long locationId) {
        this.defaultLocationId = locationId;
    }

    public String getDefaultLocationName() {
        return defaultLocationName;
    }

    public void setDefaultLocationName(String locationName) {
        this.defaultLocationName = locationName;
    }

    public Long getDefaultLessonTypeId() {
        return defaultLessonTypeId;
    }

    public void setDefaultLessonTypeId(Long lessonTypeId) {
        this.defaultLessonTypeId = lessonTypeId;
    }

    public String getDefaultLessonTypeDescription() {
        return defaultLessonTypeDescription;
    }

    public void setDefaultLessonTypeDescription(String lessonTypeDescription) {
        this.defaultLessonTypeDescription = lessonTypeDescription;
    }

    public Long getDefaultRateId() {
        return defaultRateId;
    }

    public void setDefaultRateId(Long rateId) {
        this.defaultRateId = rateId;
    }

    public String getDefaultRateDescription() {
        return defaultRateDescription;
    }

    public void setDefaultRateDescription(String rateDescription) {
        this.defaultRateDescription = rateDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PreferencesDTO preferencesDTO = (PreferencesDTO) o;
        if(preferencesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preferencesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PreferencesDTO{" +
            "id=" + getId() +
            "}";
    }
}
