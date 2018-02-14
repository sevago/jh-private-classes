package com.sevago.mpc.service.mapper;

import com.sevago.mpc.domain.*;
import com.sevago.mpc.service.dto.PreferencesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Preferences and its DTO PreferencesDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, InstructorMapper.class, ActivityMapper.class, LocationMapper.class, LessonTypeMapper.class, RateMapper.class})
public interface PreferencesMapper extends EntityMapper<PreferencesDTO, Preferences> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "defaultInstructor.id", target = "defaultInstructorId")
    @Mapping(source = "defaultInstructor.name", target = "defaultInstructorName")
    @Mapping(source = "defaultActivity.id", target = "defaultActivityId")
    @Mapping(source = "defaultActivity.name", target = "defaultActivityName")
    @Mapping(source = "defaultLocation.id", target = "defaultLocationId")
    @Mapping(source = "defaultLocation.name", target = "defaultLocationName")
    @Mapping(source = "defaultLessonType.id", target = "defaultLessonTypeId")
    @Mapping(source = "defaultLessonType.description", target = "defaultLessonTypeDescription")
    @Mapping(source = "defaultRate.id", target = "defaultRateId")
    @Mapping(source = "defaultRate.description", target = "defaultRateDescription")
    PreferencesDTO toDto(Preferences preferences); 

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "defaultInstructorId", target = "defaultInstructor")
    @Mapping(source = "defaultActivityId", target = "defaultActivity")
    @Mapping(source = "defaultLocationId", target = "defaultLocation")
    @Mapping(source = "defaultLessonTypeId", target = "defaultLessonType")
    @Mapping(source = "defaultRateId", target = "defaultRate")
    Preferences toEntity(PreferencesDTO preferencesDTO);

    default Preferences fromId(Long id) {
        if (id == null) {
            return null;
        }
        Preferences preferences = new Preferences();
        preferences.setId(id);
        return preferences;
    }
}
