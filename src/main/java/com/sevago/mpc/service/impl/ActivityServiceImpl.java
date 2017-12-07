package com.sevago.mpc.service.impl;

import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.service.ActivityService;
import com.sevago.mpc.domain.Activity;
import com.sevago.mpc.repository.ActivityRepository;
import com.sevago.mpc.repository.search.ActivitySearchRepository;
import com.sevago.mpc.service.dto.ActivityDTO;
import com.sevago.mpc.service.mapper.ActivityMapper;
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
 * Service Implementation for managing Activity.
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService{

    private final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper;

    private final ActivitySearchRepository activitySearchRepository;

    private final ApplicationProperties applicationProperties;

    public ActivityServiceImpl(ActivityRepository activityRepository, ActivityMapper activityMapper, ActivitySearchRepository activitySearchRepository, ApplicationProperties applicationProperties) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
        this.activitySearchRepository = activitySearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a activity.
     *
     * @param activityDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ActivityDTO save(ActivityDTO activityDTO) {
        log.debug("Request to save Activity : {}", activityDTO);
        Activity activity = activityMapper.toEntity(activityDTO);
        activity = activityRepository.save(activity);
        ActivityDTO result = activityMapper.toDto(activity);
        if (applicationProperties.getElasticsearch().isEnabled()) {
            activitySearchRepository.save(activity);
        }
        return result;
    }

    /**
     * Get all the activities.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> findAll() {
        log.debug("Request to get all Activities");
        return activityRepository.findByUserIsCurrentUser().stream()
            .map(activityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one activity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ActivityDTO findOne(Long id) {
        log.debug("Request to get Activity : {}", id);
        Activity activity = activityRepository.findOne(id);
        return activityMapper.toDto(activity);
    }

    /**
     * Delete the activity by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Activity : {}", id);
        activityRepository.delete(id);
        activitySearchRepository.delete(id);
    }

    /**
     * Search for the activity corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> search(String query) {
        log.debug("Request to search Activities for query {}", query);
        return StreamSupport
            .stream(activitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(activityMapper::toDto)
            .collect(Collectors.toList());
    }
}
