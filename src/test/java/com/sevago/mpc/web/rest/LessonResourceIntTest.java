package com.sevago.mpc.web.rest;

import com.sevago.mpc.PrivateclassesApp;

import com.sevago.mpc.domain.*;
import com.sevago.mpc.repository.LessonRepository;
import com.sevago.mpc.repository.UserRepository;
import com.sevago.mpc.service.ActivityService;
import com.sevago.mpc.service.LessonService;
import com.sevago.mpc.repository.search.LessonSearchRepository;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.ActivityDTO;
import com.sevago.mpc.service.dto.LessonDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.LessonMapper;
import com.sevago.mpc.web.rest.errors.ExceptionTranslator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.sevago.mpc.security.DomainUserDetailsServiceIntTest.USER;
import static com.sevago.mpc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LessonResource REST controller.
 *
 * @see LessonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
public class LessonResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_DURATION = new BigDecimal(24);
    private static final BigDecimal UPDATED_DURATION = new BigDecimal(23);

    private static final BigDecimal DEFAULT_TOTAL_CHARGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_CHARGE = new BigDecimal(2);

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonSearchRepository lessonSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    private MockMvc restLessonMockMvc;

    private Lesson lesson;

    private User user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LessonResource lessonResource = new LessonResource(lessonService);
        this.restLessonMockMvc = MockMvcBuilders.standaloneSetup(lessonResource)
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
    public static Lesson createEntity(EntityManager em) {
        Lesson lesson = new Lesson()
            .date(DEFAULT_DATE)
            .duration(DEFAULT_DURATION)
            .totalCharge(DEFAULT_TOTAL_CHARGE);
        // Add required entity
        Activity activity = ActivityResourceIntTest.createEntity(em);
        em.persist(activity);
        em.flush();
        lesson.setActivity(activity);
        // Add required entity
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        lesson.setLocation(location);
        // Add required entity
        LessonType lessonType = LessonTypeResourceIntTest.createEntity(em);
        em.persist(lessonType);
        em.flush();
        lesson.setLessonType(lessonType);
        // Add required entity
        Rate rate = RateResourceIntTest.createEntity(em);
        em.persist(rate);
        em.flush();
        lesson.setRate(rate);
        // Add required entity
        Instructor teachingInstructor = InstructorResourceIntTest.createEntity(em);
        em.persist(teachingInstructor);
        em.flush();
        lesson.setTeachingInstructor(teachingInstructor);
        return lesson;
    }

    @Before
    public void initTest() {
        lessonSearchRepository.deleteAll();
        lesson = createEntity(em);
        user = ActivityResourceIntTest.userAuthentication(userService, authenticationManager);
    }

    @After
    public void tearDown() throws Exception {
        lessonRepository.deleteAll();
        userService.deleteUser(USER);
    }

    private void createLessonsByWeek(LocalDate thisMonday, LocalDate lastMonday) {
        // Create lessons in two separate weeks
        lesson = createEntity(em);
        lesson.setDate(thisMonday.plusDays(2));
        lesson.getTeachingInstructor().setUser(user);
        lessonRepository.saveAndFlush(lesson);

        lesson = createEntity(em);
        lesson.setDate(thisMonday.plusDays(3));
        lesson.getTeachingInstructor().setUser(user);
        lessonRepository.saveAndFlush(lesson);

        lesson = createEntity(em);
        lesson.setDate(lastMonday.plusDays(3));
        lesson.getTeachingInstructor().setUser(user);
        lessonRepository.saveAndFlush(lesson);

        lesson = createEntity(em);
        lesson.setDate(lastMonday.plusDays(4));
        lesson.getTeachingInstructor().setUser(user);
        lessonRepository.saveAndFlush(lesson);
    }

    @Test
    @Transactional
    public void createLesson() throws Exception {
        int databaseSizeBeforeCreate = lessonRepository.findAll().size();

        // Create the Lesson
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);
        restLessonMockMvc.perform(post("/api/lessons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
            .andExpect(status().isCreated());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeCreate + 1);
        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testLesson.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testLesson.getTotalCharge()).isEqualTo(DEFAULT_TOTAL_CHARGE);

        // Validate the Lesson in Elasticsearch
        Lesson lessonEs = lessonSearchRepository.findOne(testLesson.getId());
        assertThat(lessonEs).isEqualToComparingFieldByField(testLesson);
    }

    @Test
    @Transactional
    public void createLessonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lessonRepository.findAll().size();

        // Create the Lesson with an existing ID
        lesson.setId(1L);
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonMockMvc.perform(post("/api/lessons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonRepository.findAll().size();
        // set the field null
        lesson.setDate(null);

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc.perform(post("/api/lessons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
            .andExpect(status().isBadRequest());

        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonRepository.findAll().size();
        // set the field null
        lesson.setDuration(null);

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc.perform(post("/api/lessons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
            .andExpect(status().isBadRequest());

        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalChargeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonRepository.findAll().size();
        // set the field null
        lesson.setTotalCharge(null);

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc.perform(post("/api/lessons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
            .andExpect(status().isBadRequest());

        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLessons() throws Exception {
        // Set user reference
        lesson.getTeachingInstructor().setUser(user);

        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList
        restLessonMockMvc.perform(get("/api/lessons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lesson.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].totalCharge").value(hasItem(DEFAULT_TOTAL_CHARGE.intValue())));
    }

    @Test
    @Transactional
    public void getLesson() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get the lesson
        restLessonMockMvc.perform(get("/api/lessons/{id}", lesson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lesson.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.intValue()))
            .andExpect(jsonPath("$.totalCharge").value(DEFAULT_TOTAL_CHARGE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLesson() throws Exception {
        // Get the lesson
        restLessonMockMvc.perform(get("/api/lessons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLesson() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);
        lessonSearchRepository.save(lesson);
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();

        // Update the lesson
        Lesson updatedLesson = lessonRepository.findOne(lesson.getId());
        // Disconnect from session so that the updates on updatedLesson are not directly saved in db
        em.detach(updatedLesson);
        updatedLesson
            .date(UPDATED_DATE)
            .duration(UPDATED_DURATION)
            .totalCharge(UPDATED_TOTAL_CHARGE);
        LessonDTO lessonDTO = lessonMapper.toDto(updatedLesson);

        restLessonMockMvc.perform(put("/api/lessons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
            .andExpect(status().isOk());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testLesson.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testLesson.getTotalCharge()).isEqualTo(UPDATED_TOTAL_CHARGE);

        // Validate the Lesson in Elasticsearch
        Lesson lessonEs = lessonSearchRepository.findOne(testLesson.getId());
        assertThat(lessonEs).isEqualToComparingFieldByField(testLesson);
    }

    @Test
    @Transactional
    public void updateNonExistingLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();

        // Create the Lesson
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLessonMockMvc.perform(put("/api/lessons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
            .andExpect(status().isCreated());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLesson() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);
        lessonSearchRepository.save(lesson);
        int databaseSizeBeforeDelete = lessonRepository.findAll().size();

        // Get the lesson
        restLessonMockMvc.perform(delete("/api/lessons/{id}", lesson.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean lessonExistsInEs = lessonSearchRepository.exists(lesson.getId());
        assertThat(lessonExistsInEs).isFalse();

        // Validate the database is empty
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLesson() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);
        lessonSearchRepository.save(lesson);

        // Search the lesson
        restLessonMockMvc.perform(get("/api/_search/lessons?query=id:" + lesson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lesson.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].totalCharge").value(hasItem(DEFAULT_TOTAL_CHARGE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lesson.class);
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        Lesson lesson2 = new Lesson();
        lesson2.setId(lesson1.getId());
        assertThat(lesson1).isEqualTo(lesson2);
        lesson2.setId(2L);
        assertThat(lesson1).isNotEqualTo(lesson2);
        lesson1.setId(null);
        assertThat(lesson1).isNotEqualTo(lesson2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonDTO.class);
        LessonDTO lessonDTO1 = new LessonDTO();
        lessonDTO1.setId(1L);
        LessonDTO lessonDTO2 = new LessonDTO();
        assertThat(lessonDTO1).isNotEqualTo(lessonDTO2);
        lessonDTO2.setId(lessonDTO1.getId());
        assertThat(lessonDTO1).isEqualTo(lessonDTO2);
        lessonDTO2.setId(2L);
        assertThat(lessonDTO1).isNotEqualTo(lessonDTO2);
        lessonDTO1.setId(null);
        assertThat(lessonDTO1).isNotEqualTo(lessonDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(lessonMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(lessonMapper.fromId(null)).isNull();
    }

    @Test
    @Transactional
    public void getLessonsByMonth() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);
        LocalDate lastMonday = thisMonday.minusDays(7);
        createLessonsByWeek(thisMonday, lastMonday);


        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        String startDate = fmt.format(today.withDayOfMonth(1));
        LocalDate firstDate = thisMonday.plusDays(2);
        LocalDate secondDate = thisMonday.plusDays(3);

        if (today.getMonthValue() < firstDate.getMonthValue() || today.getMonthValue() < secondDate.getMonthValue()) {
            firstDate = lastMonday.plusDays(3);
            secondDate = lastMonday.plusDays(4);
        }

        restLessonMockMvc.perform(get("/api/lessons/by-month/{yearWithMonth}", startDate)
            .with(user("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].date").value(hasItem(firstDate.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(secondDate.toString())));
    }
}
