package com.sevago.mpc.service.impl;

import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.repository.UserRepository;
import com.sevago.mpc.security.AuthoritiesConstants;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.StudentService;
import com.sevago.mpc.domain.Student;
import com.sevago.mpc.repository.StudentRepository;
import com.sevago.mpc.repository.search.StudentSearchRepository;
import com.sevago.mpc.service.dto.StudentDTO;
import com.sevago.mpc.service.mapper.StudentMapper;
import com.sevago.mpc.web.rest.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.stream.Stream;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Student.
 */
@Service
@Transactional
public class StudentServiceImpl extends CommonServiceImpl implements StudentService {

    private final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    private final StudentSearchRepository studentSearchRepository;

    private final ApplicationProperties applicationProperties;

    private final UserRepository userRepository;

    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper, StudentSearchRepository studentSearchRepository, ApplicationProperties applicationProperties, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.studentSearchRepository = studentSearchRepository;
        this.applicationProperties = applicationProperties;
        this.userRepository = userRepository;
    }

    /**
     * Save a student.
     *
     * @param studentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StudentDTO save(StudentDTO studentDTO) {
        log.debug("Request to save Student : {}", studentDTO);
        Student student = studentMapper.toEntity(studentDTO);
        student = (Student) populateDefaultProperties(student);
        student = studentRepository.save(student);
        StudentDTO result = studentMapper.toDto(student);
        if (applicationProperties.getElasticsearch().isEnabled()) {
            studentSearchRepository.save(student);
        }
        return result;
    }

    /**
     * Get all the students.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Students");
        return Stream.of(AuthoritiesConstants.ADMIN)
            .map(authority -> {
                if(SecurityUtils.isCurrentUserInRole(authority)) {
                    return studentRepository.findAll(pageable);
                } else {
                    return studentRepository.findByUserLoginOrderByName(SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                        new InternalServerErrorException("Current user login not found")), pageable);
                }
            })
            .map(page -> page.map(studentMapper::toDto))
            /*.reduce((acc, item) -> {
                acc.getContent().addAll(item.getContent());
                //Page page = new PageImpl(acc.getContent());
                return acc;
            })*/
            .findFirst()
            .orElseThrow(() -> new InternalServerErrorException("Missing Spring Data Page"));
    }

    /**
     * Get one student by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StudentDTO findOne(Long id) {
        log.debug("Request to get Student : {}", id);
        Student student = studentRepository.findOne(id);
        return studentMapper.toDto(student);
    }

    /**
     * Delete the student by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Student : {}", id);
        studentRepository.delete(id);
        if (applicationProperties.getElasticsearch().isEnabled()){
            studentSearchRepository.delete(id);
        }
    }

    /**
     * Search for the student corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Students for query {}", query);
        Page<Student> result = studentSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(studentMapper::toDto);
    }
}
