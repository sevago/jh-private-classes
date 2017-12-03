package com.sevago.mpc.service.impl;

import com.sevago.mpc.service.InstructorService;
import com.sevago.mpc.domain.Instructor;
import com.sevago.mpc.repository.InstructorRepository;
import com.sevago.mpc.repository.search.InstructorSearchRepository;
import com.sevago.mpc.service.dto.InstructorDTO;
import com.sevago.mpc.service.mapper.InstructorMapper;
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
 * Service Implementation for managing Instructor.
 */
@Service
@Transactional
public class InstructorServiceImpl implements InstructorService{

    private final Logger log = LoggerFactory.getLogger(InstructorServiceImpl.class);

    private final InstructorRepository instructorRepository;

    private final InstructorMapper instructorMapper;

    private final InstructorSearchRepository instructorSearchRepository;

    public InstructorServiceImpl(InstructorRepository instructorRepository, InstructorMapper instructorMapper, InstructorSearchRepository instructorSearchRepository) {
        this.instructorRepository = instructorRepository;
        this.instructorMapper = instructorMapper;
        this.instructorSearchRepository = instructorSearchRepository;
    }

    /**
     * Save a instructor.
     *
     * @param instructorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InstructorDTO save(InstructorDTO instructorDTO) {
        log.debug("Request to save Instructor : {}", instructorDTO);
        Instructor instructor = instructorMapper.toEntity(instructorDTO);
        instructor = instructorRepository.save(instructor);
        InstructorDTO result = instructorMapper.toDto(instructor);
        instructorSearchRepository.save(instructor);
        return result;
    }

    /**
     * Get all the instructors.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InstructorDTO> findAll() {
        log.debug("Request to get all Instructors");
        return instructorRepository.findAllWithEagerRelationships().stream()
            .map(instructorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one instructor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InstructorDTO findOne(Long id) {
        log.debug("Request to get Instructor : {}", id);
        Instructor instructor = instructorRepository.findOneWithEagerRelationships(id);
        return instructorMapper.toDto(instructor);
    }

    /**
     * Delete the instructor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Instructor : {}", id);
        instructorRepository.delete(id);
        instructorSearchRepository.delete(id);
    }

    /**
     * Search for the instructor corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InstructorDTO> search(String query) {
        log.debug("Request to search Instructors for query {}", query);
        return StreamSupport
            .stream(instructorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(instructorMapper::toDto)
            .collect(Collectors.toList());
    }
}