package com.sevago.mpc.service.mapper;

import com.sevago.mpc.domain.*;
import com.sevago.mpc.service.dto.InstructorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Instructor and its DTO InstructorDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, RateMapper.class})
public interface InstructorMapper extends EntityMapper<InstructorDTO, Instructor> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    InstructorDTO toDto(Instructor instructor); 

    @Mapping(target = "invoices", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(source = "userId", target = "user")
    Instructor toEntity(InstructorDTO instructorDTO);

    default Instructor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Instructor instructor = new Instructor();
        instructor.setId(id);
        return instructor;
    }
}
