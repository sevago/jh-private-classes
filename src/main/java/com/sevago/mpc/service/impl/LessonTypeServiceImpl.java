package com.sevago.mpc.service.impl;

import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.repository.UserRepository;
import com.sevago.mpc.security.AuthoritiesConstants;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.LessonTypeService;
import com.sevago.mpc.domain.LessonType;
import com.sevago.mpc.repository.LessonTypeRepository;
import com.sevago.mpc.repository.search.LessonTypeSearchRepository;
import com.sevago.mpc.service.dto.LessonTypeDTO;
import com.sevago.mpc.service.mapper.LessonTypeMapper;
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
 * Service Implementation for managing LessonType.
 */
@Service
@Transactional
public class LessonTypeServiceImpl implements LessonTypeService{

    private final Logger log = LoggerFactory.getLogger(LessonTypeServiceImpl.class);

    private final LessonTypeRepository lessonTypeRepository;

    private final LessonTypeMapper lessonTypeMapper;

    private final LessonTypeSearchRepository lessonTypeSearchRepository;

    private final ApplicationProperties applicationProperties;

    private final UserRepository userRepository;

    public LessonTypeServiceImpl(LessonTypeRepository lessonTypeRepository, LessonTypeMapper lessonTypeMapper, LessonTypeSearchRepository lessonTypeSearchRepository, ApplicationProperties applicationProperties, UserRepository userRepository) {
        this.lessonTypeRepository = lessonTypeRepository;
        this.lessonTypeMapper = lessonTypeMapper;
        this.lessonTypeSearchRepository = lessonTypeSearchRepository;
        this.applicationProperties = applicationProperties;
        this.userRepository = userRepository;
    }

    /**
     * Save a lessonType.
     *
     * @param lessonTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LessonTypeDTO save(LessonTypeDTO lessonTypeDTO) {
        log.debug("Request to save LessonType : {}", lessonTypeDTO);
        LessonType lessonType = lessonTypeMapper.toEntity(lessonTypeDTO);
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin().get());
            lessonType.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new InternalServerErrorException("Current user login not found"))
            ).get());
        }
        lessonType = lessonTypeRepository.save(lessonType);
        LessonTypeDTO result = lessonTypeMapper.toDto(lessonType);
        if (applicationProperties.getElasticsearch().isEnabled()) {
            lessonTypeSearchRepository.save(lessonType);
        }
        return result;
    }

    /**
     * Get all the lessonTypes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LessonTypeDTO> findAll() {
        log.debug("Request to get all LessonTypes");
        return Stream.of(AuthoritiesConstants.ADMIN)
            .map(authority -> {
                if(SecurityUtils.isCurrentUserInRole(authority)) {
                    return lessonTypeRepository.findAll();
                } else {
                    return lessonTypeRepository.findByUserIsCurrentUser();
                }
            })
            .flatMap(Collection::stream)
            .map(lessonTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one lessonType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LessonTypeDTO findOne(Long id) {
        log.debug("Request to get LessonType : {}", id);
        LessonType lessonType = lessonTypeRepository.findOne(id);
        return lessonTypeMapper.toDto(lessonType);
    }

    /**
     * Delete the lessonType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LessonType : {}", id);
        lessonTypeRepository.delete(id);
        if (applicationProperties.getElasticsearch().isEnabled()){
            lessonTypeSearchRepository.delete(id);
        }
    }

    /**
     * Search for the lessonType corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LessonTypeDTO> search(String query) {
        log.debug("Request to search LessonTypes for query {}", query);
        return StreamSupport
            .stream(lessonTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(lessonTypeMapper::toDto)
            .collect(Collectors.toList());
    }
}
