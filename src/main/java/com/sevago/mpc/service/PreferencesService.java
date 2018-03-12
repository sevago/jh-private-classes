package com.sevago.mpc.service;

import com.sevago.mpc.service.dto.PreferencesDTO;
import java.util.List;

/**
 * Service Interface for managing Preferences.
 */
public interface PreferencesService {

    /**
     * Save a preferences.
     *
     * @param preferencesDTO the entity to save
     * @return the persisted entity
     */
    PreferencesDTO save(PreferencesDTO preferencesDTO);

    /**
     * Get all the preferences.
     *
     * @return the list of entities
     */
    List<PreferencesDTO> findAll();

    /**
     * Get user preferences.
     *
     * @return the list of entities
     */
    List<PreferencesDTO> findUserPreferences();

    /**
     * Get the "id" preferences.
     *
     * @param id the id of the entity
     * @return the entity
     */
    PreferencesDTO findOne(Long id);

    /**
     * Delete the "id" preferences.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the preferences corresponding to the query.
     *
     * @param query the query of the search
     *
     * @return the list of entities
     */
    List<PreferencesDTO> search(String query);
}
