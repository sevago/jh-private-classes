package com.sevago.mpc.web.rest;

import com.sevago.mpc.PrivateclassesApp;

import com.sevago.mpc.domain.Rate;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.RateRepository;
import com.sevago.mpc.service.RateService;
import com.sevago.mpc.repository.search.RateSearchRepository;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.RateDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.RateMapper;
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
import java.util.List;

import static com.sevago.mpc.security.DomainUserDetailsServiceIntTest.USER;
import static com.sevago.mpc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sevago.mpc.domain.enumeration.RateCurrency;
import com.sevago.mpc.domain.enumeration.RateUnit;
/**
 * Test class for the RateResource REST controller.
 *
 * @see RateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
public class RateResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final RateCurrency DEFAULT_CURRENCY = RateCurrency.CAD;
    private static final RateCurrency UPDATED_CURRENCY = RateCurrency.USD;

    private static final RateUnit DEFAULT_UNIT = RateUnit.MINUTE;
    private static final RateUnit UPDATED_UNIT = RateUnit.HOUR;

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private RateMapper rateMapper;

    @Autowired
    private RateService rateService;

    @Autowired
    private RateSearchRepository rateSearchRepository;

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

    private MockMvc restRateMockMvc;

    private Rate rate;

    private User user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RateResource rateResource = new RateResource(rateService);
        this.restRateMockMvc = MockMvcBuilders.standaloneSetup(rateResource)
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
    public static Rate createEntity(EntityManager em) {
        Rate rate = new Rate()
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .unit(DEFAULT_UNIT);
        return rate;
    }

    @Before
    public void initTest() {
        rateSearchRepository.deleteAll();
        rate = createEntity(em);
        user = ActivityResourceIntTest.userAuthentication(userService, authenticationManager);
    }

    @After
    public void tearDown() throws Exception {
        rateRepository.deleteAll();
        userService.deleteUser(USER);
    }

    @Test
    @Transactional
    public void createRate() throws Exception {
        int databaseSizeBeforeCreate = rateRepository.findAll().size();

        // Create the Rate
        RateDTO rateDTO = rateMapper.toDto(rate);
        restRateMockMvc.perform(post("/api/rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateDTO)))
            .andExpect(status().isCreated());

        // Validate the Rate in the database
        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeCreate + 1);
        Rate testRate = rateList.get(rateList.size() - 1);
        assertThat(testRate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRate.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testRate.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testRate.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testRate.getUser()).isEqualToComparingOnlyGivenFields(user, "login");

        // Validate the Rate in Elasticsearch
        Rate rateEs = rateSearchRepository.findOne(testRate.getId());
        assertThat(rateEs).isEqualToComparingFieldByField(testRate);
    }

    @Test
    @Transactional
    public void createRateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rateRepository.findAll().size();

        // Create the Rate with an existing ID
        rate.setId(1L);
        RateDTO rateDTO = rateMapper.toDto(rate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRateMockMvc.perform(post("/api/rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rate in the database
        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = rateRepository.findAll().size();
        // set the field null
        rate.setCurrency(null);

        // Create the Rate, which fails.
        RateDTO rateDTO = rateMapper.toDto(rate);

        restRateMockMvc.perform(post("/api/rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateDTO)))
            .andExpect(status().isBadRequest());

        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = rateRepository.findAll().size();
        // set the field null
        rate.setUnit(null);

        // Create the Rate, which fails.
        RateDTO rateDTO = rateMapper.toDto(rate);

        restRateMockMvc.perform(post("/api/rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateDTO)))
            .andExpect(status().isBadRequest());

        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRates() throws Exception {
        // Set user reference
        rate.setUser(user);

        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList
        restRateMockMvc.perform(get("/api/rates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rate.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }

    @Test
    @Transactional
    public void getRate() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get the rate
        restRateMockMvc.perform(get("/api/rates/{id}", rate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rate.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRate() throws Exception {
        // Get the rate
        restRateMockMvc.perform(get("/api/rates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRate() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);
        rateSearchRepository.save(rate);
        int databaseSizeBeforeUpdate = rateRepository.findAll().size();

        // Update the rate
        Rate updatedRate = rateRepository.findOne(rate.getId());
        // Disconnect from session so that the updates on updatedRate are not directly saved in db
        em.detach(updatedRate);
        updatedRate
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .unit(UPDATED_UNIT);
        RateDTO rateDTO = rateMapper.toDto(updatedRate);

        restRateMockMvc.perform(put("/api/rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateDTO)))
            .andExpect(status().isOk());

        // Validate the Rate in the database
        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeUpdate);
        Rate testRate = rateList.get(rateList.size() - 1);
        assertThat(testRate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRate.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testRate.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testRate.getUnit()).isEqualTo(UPDATED_UNIT);

        // Validate the Rate in Elasticsearch
        Rate rateEs = rateSearchRepository.findOne(testRate.getId());
        assertThat(rateEs).isEqualToComparingFieldByField(testRate);
    }

    @Test
    @Transactional
    public void updateNonExistingRate() throws Exception {
        int databaseSizeBeforeUpdate = rateRepository.findAll().size();

        // Create the Rate
        RateDTO rateDTO = rateMapper.toDto(rate);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRateMockMvc.perform(put("/api/rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateDTO)))
            .andExpect(status().isCreated());

        // Validate the Rate in the database
        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRate() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);
        rateSearchRepository.save(rate);
        int databaseSizeBeforeDelete = rateRepository.findAll().size();

        // Get the rate
        restRateMockMvc.perform(delete("/api/rates/{id}", rate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean rateExistsInEs = rateSearchRepository.exists(rate.getId());
        assertThat(rateExistsInEs).isFalse();

        // Validate the database is empty
        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRate() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);
        rateSearchRepository.save(rate);

        // Search the rate
        restRateMockMvc.perform(get("/api/_search/rates?query=id:" + rate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rate.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rate.class);
        Rate rate1 = new Rate();
        rate1.setId(1L);
        Rate rate2 = new Rate();
        rate2.setId(rate1.getId());
        assertThat(rate1).isEqualTo(rate2);
        rate2.setId(2L);
        assertThat(rate1).isNotEqualTo(rate2);
        rate1.setId(null);
        assertThat(rate1).isNotEqualTo(rate2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RateDTO.class);
        RateDTO rateDTO1 = new RateDTO();
        rateDTO1.setId(1L);
        RateDTO rateDTO2 = new RateDTO();
        assertThat(rateDTO1).isNotEqualTo(rateDTO2);
        rateDTO2.setId(rateDTO1.getId());
        assertThat(rateDTO1).isEqualTo(rateDTO2);
        rateDTO2.setId(2L);
        assertThat(rateDTO1).isNotEqualTo(rateDTO2);
        rateDTO1.setId(null);
        assertThat(rateDTO1).isNotEqualTo(rateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rateMapper.fromId(null)).isNull();
    }
}
