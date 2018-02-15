package com.sevago.mpc.service.impl;

import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.repository.UserRepository;
import com.sevago.mpc.security.AuthoritiesConstants;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.PreferencesService;
import com.sevago.mpc.domain.Preferences;
import com.sevago.mpc.repository.PreferencesRepository;
import com.sevago.mpc.repository.search.PreferencesSearchRepository;
import com.sevago.mpc.service.dto.PreferencesDTO;
import com.sevago.mpc.service.mapper.PreferencesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Preferences.
 */
@Service
@Transactional
public class PreferencesServiceImpl extends CommonServiceImpl implements PreferencesService{

    private final Logger log = LoggerFactory.getLogger(PreferencesServiceImpl.class);

    private final PreferencesRepository preferencesRepository;

    private final PreferencesMapper preferencesMapper;

    private final PreferencesSearchRepository preferencesSearchRepository;

    private final ApplicationProperties applicationProperties;

    public PreferencesServiceImpl(PreferencesRepository preferencesRepository, PreferencesMapper preferencesMapper, PreferencesSearchRepository preferencesSearchRepository, ApplicationProperties applicationProperties, UserRepository userRepository) {
        this.preferencesRepository = preferencesRepository;
        this.preferencesMapper = preferencesMapper;
        this.preferencesSearchRepository = preferencesSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a preferences.
     *
     * @param preferencesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PreferencesDTO save(PreferencesDTO preferencesDTO) {
        log.debug("Request to save Preferences : {}", preferencesDTO);
        Preferences preferences = preferencesMapper.toEntity(preferencesDTO);
        preferences = (Preferences) populateDefaultProperties(preferences);
        preferences = preferencesRepository.save(preferences);
        PreferencesDTO result = preferencesMapper.toDto(preferences);
        if (applicationProperties.getElasticsearch().isEnabled()) {
            preferencesSearchRepository.save(preferences);
        }
        return result;
    }

    /**
     * Get all the preferences.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PreferencesDTO> findAll() {
        log.debug("Request to get all Preferences");
        return Stream.of(AuthoritiesConstants.ADMIN)
            .map(authority -> {
                if(SecurityUtils.isCurrentUserInRole(authority)) {
                    return preferencesRepository.findAll();
                } else {
                    return preferencesRepository.findByUserIsCurrentUser();
                }
            })
            .flatMap(Collection::stream)
            .map(preferencesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one preferences by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PreferencesDTO findOne(Long id) {
        log.debug("Request to get Preferences : {}", id);
        Preferences preferences = preferencesRepository.findOne(id);
        return preferencesMapper.toDto(preferences);
    }

    /**
     * Delete the preferences by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Preferences : {}", id);
        preferencesRepository.delete(id);
        if (applicationProperties.getElasticsearch().isEnabled()) {
            preferencesSearchRepository.delete(id);
        }
    }

    /**
     * Search for the preferences corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PreferencesDTO> search(String query) {
        log.debug("Request to search Preferences for query {}", query);
        return StreamSupport
            .stream(preferencesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(preferencesMapper::toDto)
            .collect(Collectors.toList());
    }
}
