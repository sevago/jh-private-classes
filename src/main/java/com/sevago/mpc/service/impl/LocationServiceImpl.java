package com.sevago.mpc.service.impl;

import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.service.LocationService;
import com.sevago.mpc.domain.Location;
import com.sevago.mpc.repository.LocationRepository;
import com.sevago.mpc.repository.search.LocationSearchRepository;
import com.sevago.mpc.service.dto.LocationDTO;
import com.sevago.mpc.service.mapper.LocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Location.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService{

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    private final LocationSearchRepository locationSearchRepository;

    private final ApplicationProperties applicationProperties;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper, LocationSearchRepository locationSearchRepository, ApplicationProperties applicationProperties) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.locationSearchRepository = locationSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a location.
     *
     * @param locationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LocationDTO save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);
        Location location = locationMapper.toEntity(locationDTO);
        location = locationRepository.save(location);
        LocationDTO result = locationMapper.toDto(location);
        if (applicationProperties.getElasticsearch().isEnabled()) {
            locationSearchRepository.save(location);
        }
        return result;
    }

    /**
     * Get all the locations.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocationDTO> findAll() {
        log.debug("Request to get all Locations");
        return locationRepository.findByUserIsCurrentUser().stream()
            .map(locationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one location by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LocationDTO findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        Location location = locationRepository.findOne(id);
        return locationMapper.toDto(location);
    }

    /**
     * Delete the location by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.delete(id);
        if (applicationProperties.getElasticsearch().isEnabled()){
            locationSearchRepository.delete(id);
        }
    }

    /**
     * Search for the location corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocationDTO> search(String query) {
        log.debug("Request to search Locations for query {}", query);
        return StreamSupport
            .stream(locationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(locationMapper::toDto)
            .collect(Collectors.toList());
    }
}
