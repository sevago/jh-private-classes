package com.sevago.mpc.service.impl;

import com.sevago.mpc.service.LessonTypeService;
import com.sevago.mpc.domain.LessonType;
import com.sevago.mpc.repository.LessonTypeRepository;
import com.sevago.mpc.repository.search.LessonTypeSearchRepository;
import com.sevago.mpc.service.dto.LessonTypeDTO;
import com.sevago.mpc.service.mapper.LessonTypeMapper;
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
 * Service Implementation for managing LessonType.
 */
@Service
@Transactional
public class LessonTypeServiceImpl implements LessonTypeService{

    private final Logger log = LoggerFactory.getLogger(LessonTypeServiceImpl.class);

    private final LessonTypeRepository lessonTypeRepository;

    private final LessonTypeMapper lessonTypeMapper;

    private final LessonTypeSearchRepository lessonTypeSearchRepository;

    public LessonTypeServiceImpl(LessonTypeRepository lessonTypeRepository, LessonTypeMapper lessonTypeMapper, LessonTypeSearchRepository lessonTypeSearchRepository) {
        this.lessonTypeRepository = lessonTypeRepository;
        this.lessonTypeMapper = lessonTypeMapper;
        this.lessonTypeSearchRepository = lessonTypeSearchRepository;
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
        lessonType = lessonTypeRepository.save(lessonType);
        LessonTypeDTO result = lessonTypeMapper.toDto(lessonType);
        lessonTypeSearchRepository.save(lessonType);
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
        return lessonTypeRepository.findAll().stream()
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
        lessonTypeSearchRepository.delete(id);
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
