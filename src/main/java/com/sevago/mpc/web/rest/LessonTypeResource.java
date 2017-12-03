package com.sevago.mpc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sevago.mpc.service.LessonTypeService;
import com.sevago.mpc.web.rest.errors.BadRequestAlertException;
import com.sevago.mpc.web.rest.util.HeaderUtil;
import com.sevago.mpc.service.dto.LessonTypeDTO;
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
 * REST controller for managing LessonType.
 */
@RestController
@RequestMapping("/api")
public class LessonTypeResource {

    private final Logger log = LoggerFactory.getLogger(LessonTypeResource.class);

    private static final String ENTITY_NAME = "lessonType";

    private final LessonTypeService lessonTypeService;

    public LessonTypeResource(LessonTypeService lessonTypeService) {
        this.lessonTypeService = lessonTypeService;
    }

    /**
     * POST  /lesson-types : Create a new lessonType.
     *
     * @param lessonTypeDTO the lessonTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lessonTypeDTO, or with status 400 (Bad Request) if the lessonType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lesson-types")
    @Timed
    public ResponseEntity<LessonTypeDTO> createLessonType(@Valid @RequestBody LessonTypeDTO lessonTypeDTO) throws URISyntaxException {
        log.debug("REST request to save LessonType : {}", lessonTypeDTO);
        if (lessonTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new lessonType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LessonTypeDTO result = lessonTypeService.save(lessonTypeDTO);
        return ResponseEntity.created(new URI("/api/lesson-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lesson-types : Updates an existing lessonType.
     *
     * @param lessonTypeDTO the lessonTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lessonTypeDTO,
     * or with status 400 (Bad Request) if the lessonTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the lessonTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lesson-types")
    @Timed
    public ResponseEntity<LessonTypeDTO> updateLessonType(@Valid @RequestBody LessonTypeDTO lessonTypeDTO) throws URISyntaxException {
        log.debug("REST request to update LessonType : {}", lessonTypeDTO);
        if (lessonTypeDTO.getId() == null) {
            return createLessonType(lessonTypeDTO);
        }
        LessonTypeDTO result = lessonTypeService.save(lessonTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lessonTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lesson-types : get all the lessonTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lessonTypes in body
     */
    @GetMapping("/lesson-types")
    @Timed
    public List<LessonTypeDTO> getAllLessonTypes() {
        log.debug("REST request to get all LessonTypes");
        return lessonTypeService.findAll();
        }

    /**
     * GET  /lesson-types/:id : get the "id" lessonType.
     *
     * @param id the id of the lessonTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lessonTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/lesson-types/{id}")
    @Timed
    public ResponseEntity<LessonTypeDTO> getLessonType(@PathVariable Long id) {
        log.debug("REST request to get LessonType : {}", id);
        LessonTypeDTO lessonTypeDTO = lessonTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(lessonTypeDTO));
    }

    /**
     * DELETE  /lesson-types/:id : delete the "id" lessonType.
     *
     * @param id the id of the lessonTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lesson-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteLessonType(@PathVariable Long id) {
        log.debug("REST request to delete LessonType : {}", id);
        lessonTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/lesson-types?query=:query : search for the lessonType corresponding
     * to the query.
     *
     * @param query the query of the lessonType search
     * @return the result of the search
     */
    @GetMapping("/_search/lesson-types")
    @Timed
    public List<LessonTypeDTO> searchLessonTypes(@RequestParam String query) {
        log.debug("REST request to search LessonTypes for query {}", query);
        return lessonTypeService.search(query);
    }

}
