package com.sevago.mpc.service.impl;

import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.repository.UserRepository;
import com.sevago.mpc.security.AuthoritiesConstants;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.RateService;
import com.sevago.mpc.domain.Rate;
import com.sevago.mpc.repository.RateRepository;
import com.sevago.mpc.repository.search.RateSearchRepository;
import com.sevago.mpc.service.dto.RateDTO;
import com.sevago.mpc.service.mapper.RateMapper;
import com.sevago.mpc.web.rest.errors.InternalServerErrorException;
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
 * Service Implementation for managing Rate.
 */
@Service
@Transactional
public class RateServiceImpl implements RateService{

    private final Logger log = LoggerFactory.getLogger(RateServiceImpl.class);

    private final RateRepository rateRepository;

    private final RateMapper rateMapper;

    private final RateSearchRepository rateSearchRepository;

    private final ApplicationProperties applicationProperties;

    private final UserRepository userRepository;

    public RateServiceImpl(RateRepository rateRepository, RateMapper rateMapper, RateSearchRepository rateSearchRepository, ApplicationProperties applicationProperties, UserRepository userRepository) {
        this.rateRepository = rateRepository;
        this.rateMapper = rateMapper;
        this.rateSearchRepository = rateSearchRepository;
        this.applicationProperties = applicationProperties;
        this.userRepository = userRepository;
    }

    /**
     * Save a rate.
     *
     * @param rateDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RateDTO save(RateDTO rateDTO) {
        log.debug("Request to save Rate : {}", rateDTO);
        Rate rate = rateMapper.toEntity(rateDTO);
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin().get());
            rate.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new InternalServerErrorException("Current user login not found"))
            ).get());
        }
        rate = rateRepository.save(rate);
        RateDTO result = rateMapper.toDto(rate);
        if (applicationProperties.getElasticsearch().isEnabled()) {
            rateSearchRepository.save(rate);
        }
        return result;
    }

    /**
     * Get all the rates.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RateDTO> findAll() {
        log.debug("Request to get all Rates");
        return Stream.of(AuthoritiesConstants.ADMIN)
            .map(authority -> {
                if(SecurityUtils.isCurrentUserInRole(authority)) {
                    return rateRepository.findAll();
                } else {
                    return rateRepository.findByUserIsCurrentUser();
                }
            })
            .flatMap(Collection::stream)
            .map(rateMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one rate by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RateDTO findOne(Long id) {
        log.debug("Request to get Rate : {}", id);
        Rate rate = rateRepository.findOne(id);
        return rateMapper.toDto(rate);
    }

    /**
     * Delete the rate by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rate : {}", id);
        rateRepository.delete(id);
        if (applicationProperties.getElasticsearch().isEnabled()){
            rateSearchRepository.delete(id);
        }
    }

    /**
     * Search for the rate corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RateDTO> search(String query) {
        log.debug("Request to search Rates for query {}", query);
        return StreamSupport
            .stream(rateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(rateMapper::toDto)
            .collect(Collectors.toList());
    }
}
