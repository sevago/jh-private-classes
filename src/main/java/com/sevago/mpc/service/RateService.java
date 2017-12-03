package com.sevago.mpc.service;

import com.sevago.mpc.service.dto.RateDTO;
import java.util.List;

/**
 * Service Interface for managing Rate.
 */
public interface RateService {

    /**
     * Save a rate.
     *
     * @param rateDTO the entity to save
     * @return the persisted entity
     */
    RateDTO save(RateDTO rateDTO);

    /**
     * Get all the rates.
     *
     * @return the list of entities
     */
    List<RateDTO> findAll();

    /**
     * Get the "id" rate.
     *
     * @param id the id of the entity
     * @return the entity
     */
    RateDTO findOne(Long id);

    /**
     * Delete the "id" rate.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the rate corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RateDTO> search(String query);
}
