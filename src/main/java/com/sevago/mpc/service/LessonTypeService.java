package com.sevago.mpc.service;

import com.sevago.mpc.service.dto.LessonTypeDTO;
import java.util.List;

/**
 * Service Interface for managing LessonType.
 */
public interface LessonTypeService {

    /**
     * Save a lessonType.
     *
     * @param lessonTypeDTO the entity to save
     * @return the persisted entity
     */
    LessonTypeDTO save(LessonTypeDTO lessonTypeDTO);

    /**
     * Get all the lessonTypes.
     *
     * @return the list of entities
     */
    List<LessonTypeDTO> findAll();

    /**
     * Get the "id" lessonType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    LessonTypeDTO findOne(Long id);

    /**
     * Delete the "id" lessonType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the lessonType corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<LessonTypeDTO> search(String query);
}
