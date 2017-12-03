package com.sevago.mpc.web.rest;

import com.sevago.mpc.PrivateclassesApp;

import com.sevago.mpc.domain.LessonType;
import com.sevago.mpc.repository.LessonTypeRepository;
import com.sevago.mpc.service.LessonTypeService;
import com.sevago.mpc.repository.search.LessonTypeSearchRepository;
import com.sevago.mpc.service.dto.LessonTypeDTO;
import com.sevago.mpc.service.mapper.LessonTypeMapper;
import com.sevago.mpc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static com.sevago.mpc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LessonTypeResource REST controller.
 *
 * @see LessonTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
public class LessonTypeResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_RATIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_RATIO = new BigDecimal(0);

    @Autowired
    private LessonTypeRepository lessonTypeRepository;

    @Autowired
    private LessonTypeMapper lessonTypeMapper;

    @Autowired
    private LessonTypeService lessonTypeService;

    @Autowired
    private LessonTypeSearchRepository lessonTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLessonTypeMockMvc;

    private LessonType lessonType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LessonTypeResource lessonTypeResource = new LessonTypeResource(lessonTypeService);
        this.restLessonTypeMockMvc = MockMvcBuilders.standaloneSetup(lessonTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonType createEntity(EntityManager em) {
        LessonType lessonType = new LessonType()
            .description(DEFAULT_DESCRIPTION)
            .ratio(DEFAULT_RATIO);
        return lessonType;
    }

    @Before
    public void initTest() {
        lessonTypeSearchRepository.deleteAll();
        lessonType = createEntity(em);
    }

    @Test
    @Transactional
    public void createLessonType() throws Exception {
        int databaseSizeBeforeCreate = lessonTypeRepository.findAll().size();

        // Create the LessonType
        LessonTypeDTO lessonTypeDTO = lessonTypeMapper.toDto(lessonType);
        restLessonTypeMockMvc.perform(post("/api/lesson-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the LessonType in the database
        List<LessonType> lessonTypeList = lessonTypeRepository.findAll();
        assertThat(lessonTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LessonType testLessonType = lessonTypeList.get(lessonTypeList.size() - 1);
        assertThat(testLessonType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLessonType.getRatio()).isEqualTo(DEFAULT_RATIO);

        // Validate the LessonType in Elasticsearch
        LessonType lessonTypeEs = lessonTypeSearchRepository.findOne(testLessonType.getId());
        assertThat(lessonTypeEs).isEqualToComparingFieldByField(testLessonType);
    }

    @Test
    @Transactional
    public void createLessonTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lessonTypeRepository.findAll().size();

        // Create the LessonType with an existing ID
        lessonType.setId(1L);
        LessonTypeDTO lessonTypeDTO = lessonTypeMapper.toDto(lessonType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonTypeMockMvc.perform(post("/api/lesson-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LessonType in the database
        List<LessonType> lessonTypeList = lessonTypeRepository.findAll();
        assertThat(lessonTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonTypeRepository.findAll().size();
        // set the field null
        lessonType.setDescription(null);

        // Create the LessonType, which fails.
        LessonTypeDTO lessonTypeDTO = lessonTypeMapper.toDto(lessonType);

        restLessonTypeMockMvc.perform(post("/api/lesson-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonTypeDTO)))
            .andExpect(status().isBadRequest());

        List<LessonType> lessonTypeList = lessonTypeRepository.findAll();
        assertThat(lessonTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRatioIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonTypeRepository.findAll().size();
        // set the field null
        lessonType.setRatio(null);

        // Create the LessonType, which fails.
        LessonTypeDTO lessonTypeDTO = lessonTypeMapper.toDto(lessonType);

        restLessonTypeMockMvc.perform(post("/api/lesson-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonTypeDTO)))
            .andExpect(status().isBadRequest());

        List<LessonType> lessonTypeList = lessonTypeRepository.findAll();
        assertThat(lessonTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLessonTypes() throws Exception {
        // Initialize the database
        lessonTypeRepository.saveAndFlush(lessonType);

        // Get all the lessonTypeList
        restLessonTypeMockMvc.perform(get("/api/lesson-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonType.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].ratio").value(hasItem(DEFAULT_RATIO.intValue())));
    }

    @Test
    @Transactional
    public void getLessonType() throws Exception {
        // Initialize the database
        lessonTypeRepository.saveAndFlush(lessonType);

        // Get the lessonType
        restLessonTypeMockMvc.perform(get("/api/lesson-types/{id}", lessonType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lessonType.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.ratio").value(DEFAULT_RATIO.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLessonType() throws Exception {
        // Get the lessonType
        restLessonTypeMockMvc.perform(get("/api/lesson-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLessonType() throws Exception {
        // Initialize the database
        lessonTypeRepository.saveAndFlush(lessonType);
        lessonTypeSearchRepository.save(lessonType);
        int databaseSizeBeforeUpdate = lessonTypeRepository.findAll().size();

        // Update the lessonType
        LessonType updatedLessonType = lessonTypeRepository.findOne(lessonType.getId());
        // Disconnect from session so that the updates on updatedLessonType are not directly saved in db
        em.detach(updatedLessonType);
        updatedLessonType
            .description(UPDATED_DESCRIPTION)
            .ratio(UPDATED_RATIO);
        LessonTypeDTO lessonTypeDTO = lessonTypeMapper.toDto(updatedLessonType);

        restLessonTypeMockMvc.perform(put("/api/lesson-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonTypeDTO)))
            .andExpect(status().isOk());

        // Validate the LessonType in the database
        List<LessonType> lessonTypeList = lessonTypeRepository.findAll();
        assertThat(lessonTypeList).hasSize(databaseSizeBeforeUpdate);
        LessonType testLessonType = lessonTypeList.get(lessonTypeList.size() - 1);
        assertThat(testLessonType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLessonType.getRatio()).isEqualTo(UPDATED_RATIO);

        // Validate the LessonType in Elasticsearch
        LessonType lessonTypeEs = lessonTypeSearchRepository.findOne(testLessonType.getId());
        assertThat(lessonTypeEs).isEqualToComparingFieldByField(testLessonType);
    }

    @Test
    @Transactional
    public void updateNonExistingLessonType() throws Exception {
        int databaseSizeBeforeUpdate = lessonTypeRepository.findAll().size();

        // Create the LessonType
        LessonTypeDTO lessonTypeDTO = lessonTypeMapper.toDto(lessonType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLessonTypeMockMvc.perform(put("/api/lesson-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the LessonType in the database
        List<LessonType> lessonTypeList = lessonTypeRepository.findAll();
        assertThat(lessonTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLessonType() throws Exception {
        // Initialize the database
        lessonTypeRepository.saveAndFlush(lessonType);
        lessonTypeSearchRepository.save(lessonType);
        int databaseSizeBeforeDelete = lessonTypeRepository.findAll().size();

        // Get the lessonType
        restLessonTypeMockMvc.perform(delete("/api/lesson-types/{id}", lessonType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean lessonTypeExistsInEs = lessonTypeSearchRepository.exists(lessonType.getId());
        assertThat(lessonTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<LessonType> lessonTypeList = lessonTypeRepository.findAll();
        assertThat(lessonTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLessonType() throws Exception {
        // Initialize the database
        lessonTypeRepository.saveAndFlush(lessonType);
        lessonTypeSearchRepository.save(lessonType);

        // Search the lessonType
        restLessonTypeMockMvc.perform(get("/api/_search/lesson-types?query=id:" + lessonType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonType.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].ratio").value(hasItem(DEFAULT_RATIO.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonType.class);
        LessonType lessonType1 = new LessonType();
        lessonType1.setId(1L);
        LessonType lessonType2 = new LessonType();
        lessonType2.setId(lessonType1.getId());
        assertThat(lessonType1).isEqualTo(lessonType2);
        lessonType2.setId(2L);
        assertThat(lessonType1).isNotEqualTo(lessonType2);
        lessonType1.setId(null);
        assertThat(lessonType1).isNotEqualTo(lessonType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonTypeDTO.class);
        LessonTypeDTO lessonTypeDTO1 = new LessonTypeDTO();
        lessonTypeDTO1.setId(1L);
        LessonTypeDTO lessonTypeDTO2 = new LessonTypeDTO();
        assertThat(lessonTypeDTO1).isNotEqualTo(lessonTypeDTO2);
        lessonTypeDTO2.setId(lessonTypeDTO1.getId());
        assertThat(lessonTypeDTO1).isEqualTo(lessonTypeDTO2);
        lessonTypeDTO2.setId(2L);
        assertThat(lessonTypeDTO1).isNotEqualTo(lessonTypeDTO2);
        lessonTypeDTO1.setId(null);
        assertThat(lessonTypeDTO1).isNotEqualTo(lessonTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(lessonTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(lessonTypeMapper.fromId(null)).isNull();
    }
}
