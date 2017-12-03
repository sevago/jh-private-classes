package com.sevago.mpc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sevago.mpc.service.InstructorService;
import com.sevago.mpc.web.rest.errors.BadRequestAlertException;
import com.sevago.mpc.web.rest.util.HeaderUtil;
import com.sevago.mpc.service.dto.InstructorDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Instructor.
 */
@RestController
@RequestMapping("/api")
public class InstructorResource {

    private final Logger log = LoggerFactory.getLogger(InstructorResource.class);

    private static final String ENTITY_NAME = "instructor";

    private final InstructorService instructorService;

    public InstructorResource(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    /**
     * POST  /instructors : Create a new instructor.
     *
     * @param instructorDTO the instructorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instructorDTO, or with status 400 (Bad Request) if the instructor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/instructors")
    @Timed
    public ResponseEntity<InstructorDTO> createInstructor(@Valid @RequestBody InstructorDTO instructorDTO) throws URISyntaxException {
        log.debug("REST request to save Instructor : {}", instructorDTO);
        if (instructorDTO.getId() != null) {
            throw new BadRequestAlertException("A new instructor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InstructorDTO result = instructorService.save(instructorDTO);
        return ResponseEntity.created(new URI("/api/instructors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instructors : Updates an existing instructor.
     *
     * @param instructorDTO the instructorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instructorDTO,
     * or with status 400 (Bad Request) if the instructorDTO is not valid,
     * or with status 500 (Internal Server Error) if the instructorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/instructors")
    @Timed
    public ResponseEntity<InstructorDTO> updateInstructor(@Valid @RequestBody InstructorDTO instructorDTO) throws URISyntaxException {
        log.debug("REST request to update Instructor : {}", instructorDTO);
        if (instructorDTO.getId() == null) {
            return createInstructor(instructorDTO);
        }
        InstructorDTO result = instructorService.save(instructorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, instructorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instructors : get all the instructors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of instructors in body
     */
    @GetMapping("/instructors")
    @Timed
    public List<InstructorDTO> getAllInstructors() {
        log.debug("REST request to get all Instructors");
        return instructorService.findAll();
        }

    /**
     * GET  /instructors/:id : get the "id" instructor.
     *
     * @param id the id of the instructorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instructorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/instructors/{id}")
    @Timed
    public ResponseEntity<InstructorDTO> getInstructor(@PathVariable Long id) {
        log.debug("REST request to get Instructor : {}", id);
        InstructorDTO instructorDTO = instructorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(instructorDTO));
    }

    /**
     * DELETE  /instructors/:id : delete the "id" instructor.
     *
     * @param id the id of the instructorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/instructors/{id}")
    @Timed
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        log.debug("REST request to delete Instructor : {}", id);
        instructorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/instructors?query=:query : search for the instructor corresponding
     * to the query.
     *
     * @param query the query of the instructor search
     * @return the result of the search
     */
    @GetMapping("/_search/instructors")
    @Timed
    public List<InstructorDTO> searchInstructors(@RequestParam String query) {
        log.debug("REST request to search Instructors for query {}", query);
        return instructorService.search(query);
    }

}
