package com.sevago.mpc.service;

import com.sevago.mpc.service.dto.InstructorDTO;
import java.util.List;

/**
 * Service Interface for managing Instructor.
 */
public interface InstructorService {

    /**
     * Save a instructor.
     *
     * @param instructorDTO the entity to save
     * @return the persisted entity
     */
    InstructorDTO save(InstructorDTO instructorDTO);

    /**
     * Get all the instructors.
     *
     * @return the list of entities
     */
    List<InstructorDTO> findAll();

    /**
     * Get the "id" instructor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    InstructorDTO findOne(Long id);

    /**
     * Delete the "id" instructor.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the instructor corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<InstructorDTO> search(String query);
}
