package com.sevago.mpc.service.mapper;

import com.sevago.mpc.domain.*;
import com.sevago.mpc.service.dto.LessonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Lesson and its DTO LessonDTO.
 */
@Mapper(componentModel = "spring", uses = {ActivityMapper.class, LocationMapper.class, LessonTypeMapper.class, RateMapper.class, StudentMapper.class, InstructorMapper.class})
public interface LessonMapper extends EntityMapper<LessonDTO, Lesson> {

    @Mapping(source = "activity.id", target = "activityId")
    @Mapping(source = "activity.name", target = "activityName")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(source = "lessonType.id", target = "lessonTypeId")
    @Mapping(source = "lessonType.description", target = "lessonTypeDescription")
    @Mapping(source = "rate.id", target = "rateId")
    @Mapping(source = "rate.description", target = "rateDescription")
    @Mapping(source = "teachingInstructor.id", target = "teachingInstructorId")
    @Mapping(source = "teachingInstructor.name", target = "teachingInstructorName")
    @Mapping(source = "teachingInstructor.user.id", target = "userId")
    LessonDTO toDto(Lesson lesson);

    @Mapping(source = "activityId", target = "activity")
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "lessonTypeId", target = "lessonType")
    @Mapping(source = "rateId", target = "rate")
    @Mapping(source = "teachingInstructorId", target = "teachingInstructor")
    @Mapping(target = "invoices", ignore = true)
    Lesson toEntity(LessonDTO lessonDTO);

    default Lesson fromId(Long id) {
        if (id == null) {
            return null;
        }
        Lesson lesson = new Lesson();
        lesson.setId(id);
        return lesson;
    }
}
