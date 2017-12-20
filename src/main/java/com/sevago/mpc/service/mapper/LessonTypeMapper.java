package com.sevago.mpc.service.mapper;

import com.sevago.mpc.domain.*;
import com.sevago.mpc.service.dto.LessonTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LessonType and its DTO LessonTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LessonTypeMapper extends EntityMapper<LessonTypeDTO, LessonType> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    LessonTypeDTO toDto(LessonType lessonType);

    @Mapping(source = "userId", target = "user")
    LessonType toEntity(LessonTypeDTO lessonTypeDTO);

    default LessonType fromId(Long id) {
        if (id == null) {
            return null;
        }
        LessonType lessonType = new LessonType();
        lessonType.setId(id);
        return lessonType;
    }
}
