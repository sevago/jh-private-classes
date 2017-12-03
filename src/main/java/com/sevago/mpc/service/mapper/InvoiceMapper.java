package com.sevago.mpc.service.mapper;

import com.sevago.mpc.domain.*;
import com.sevago.mpc.service.dto.InvoiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Invoice and its DTO InvoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {LessonMapper.class, StudentMapper.class, InstructorMapper.class})
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {

    @Mapping(source = "billToStudent.id", target = "billToStudentId")
    @Mapping(source = "billToStudent.name", target = "billToStudentName")
    @Mapping(source = "teachingInstructor.id", target = "teachingInstructorId")
    @Mapping(source = "teachingInstructor.name", target = "teachingInstructorName")
    InvoiceDTO toDto(Invoice invoice); 

    @Mapping(source = "billToStudentId", target = "billToStudent")
    @Mapping(source = "teachingInstructorId", target = "teachingInstructor")
    Invoice toEntity(InvoiceDTO invoiceDTO);

    default Invoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setId(id);
        return invoice;
    }
}
