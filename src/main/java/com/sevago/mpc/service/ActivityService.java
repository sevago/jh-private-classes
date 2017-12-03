package com.sevago.mpc.service;

import com.sevago.mpc.service.dto.ActivityDTO;
import java.util.List;

/**
 * Service Interface for managing Activity.
 */
public interface ActivityService {

    /**
     * Save a activity.
     *
     * @param activityDTO the entity to save
     * @return the persisted entity
     */
    ActivityDTO save(ActivityDTO activityDTO);

    /**
     * Get all the activities.
     *
     * @return the list of entities
     */
    List<ActivityDTO> findAll();

    /**
     * Get the "id" activity.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ActivityDTO findOne(Long id);

    /**
     * Delete the "id" activity.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the activity corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<ActivityDTO> search(String query);
}
