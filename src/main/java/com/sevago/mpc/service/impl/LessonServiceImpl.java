package com.sevago.mpc.service.impl;

import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.LessonService;
import com.sevago.mpc.domain.Lesson;
import com.sevago.mpc.repository.LessonRepository;
import com.sevago.mpc.repository.search.LessonSearchRepository;
import com.sevago.mpc.service.dto.LessonDTO;
import com.sevago.mpc.service.mapper.LessonMapper;
import com.sevago.mpc.web.rest.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Lesson.
 */
@Service
@Transactional
public class LessonServiceImpl implements LessonService{

    private final Logger log = LoggerFactory.getLogger(LessonServiceImpl.class);

    private final LessonRepository lessonRepository;

    private final LessonMapper lessonMapper;

    private final LessonSearchRepository lessonSearchRepository;

    private final ApplicationProperties applicationProperties;

    public LessonServiceImpl(LessonRepository lessonRepository, LessonMapper lessonMapper, LessonSearchRepository lessonSearchRepository, ApplicationProperties applicationProperties) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.lessonSearchRepository = lessonSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a lesson.
     *
     * @param lessonDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LessonDTO save(LessonDTO lessonDTO) {
        log.debug("Request to save Lesson : {}", lessonDTO);
        Lesson lesson = lessonMapper.toEntity(lessonDTO);
        lesson = lessonRepository.save(lesson);
        LessonDTO result = lessonMapper.toDto(lesson);
        if (applicationProperties.getElasticsearch().isEnabled()) {
            lessonSearchRepository.save(lesson);
        }
        return result;
    }

    /**
     * Get all the lessons.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LessonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lessons");
        return lessonRepository.findByTeachingInstructorUserLoginOrderByDateDesc(SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
            new InternalServerErrorException("Current user login not found")), pageable)
            .map(lessonMapper::toDto);
    }

    /**
     * Get one lesson by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LessonDTO findOne(Long id) {
        log.debug("Request to get Lesson : {}", id);
        Lesson lesson = lessonRepository.findOneWithEagerRelationships(id);
        return lessonMapper.toDto(lesson);
    }

    /**
     * Delete the lesson by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lesson : {}", id);
        lessonRepository.delete(id);
        if (applicationProperties.getElasticsearch().isEnabled()){
            lessonSearchRepository.delete(id);
        }
    }

    /**
     * Search for the lesson corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LessonDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Lessons for query {}", query);
        Page<Lesson> result = lessonSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(lessonMapper::toDto);
    }
}
