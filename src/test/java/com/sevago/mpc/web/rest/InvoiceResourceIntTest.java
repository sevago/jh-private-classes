package com.sevago.mpc.web.rest;

import com.sevago.mpc.PrivateclassesApp;

import com.sevago.mpc.domain.Invoice;
import com.sevago.mpc.domain.Student;
import com.sevago.mpc.domain.Instructor;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.InvoiceRepository;
import com.sevago.mpc.service.InvoiceService;
import com.sevago.mpc.repository.search.InvoiceSearchRepository;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.InvoiceDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.InvoiceMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.sevago.mpc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sevago.mpc.domain.enumeration.RateCurrency;
/**
 * Test class for the InvoiceResource REST controller.
 *
 * @see InvoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
public class InvoiceResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final LocalDate DEFAULT_PERIOD_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIOD_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ISSUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ISSUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);

    private static final RateCurrency DEFAULT_TOTAL_CURRENCY = RateCurrency.CAD;
    private static final RateCurrency UPDATED_TOTAL_CURRENCY = RateCurrency.USD;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceSearchRepository invoiceSearchRepository;

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

    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    private static UserDTO userDTO;

    private static User user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceResource invoiceResource = new InvoiceResource(invoiceService);
        this.restInvoiceMockMvc = MockMvcBuilders.standaloneSetup(invoiceResource)
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
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .number(DEFAULT_NUMBER)
            .periodStartDate(DEFAULT_PERIOD_START_DATE)
            .periodEndDate(DEFAULT_PERIOD_END_DATE)
            .issueDate(DEFAULT_ISSUE_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .totalCurrency(DEFAULT_TOTAL_CURRENCY);
        // Add required entity
        Student billToStudent = StudentResourceIntTest.createEntity(em);
        em.persist(billToStudent);
        em.flush();
        invoice.setBillToStudent(billToStudent);
        // Add required entity
        Instructor teachingInstructor = InstructorResourceIntTest.createEntity(em);
        em.persist(teachingInstructor);
        em.flush();
        invoice.setTeachingInstructor(teachingInstructor);
        return invoice;
    }

    @Before
    public void initTest() {
        invoiceSearchRepository.deleteAll();
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testInvoice.getPeriodStartDate()).isEqualTo(DEFAULT_PERIOD_START_DATE);
        assertThat(testInvoice.getPeriodEndDate()).isEqualTo(DEFAULT_PERIOD_END_DATE);
        assertThat(testInvoice.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testInvoice.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testInvoice.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testInvoice.getTotalCurrency()).isEqualTo(DEFAULT_TOTAL_CURRENCY);

        // Validate the Invoice in Elasticsearch
        Invoice invoiceEs = invoiceSearchRepository.findOne(testInvoice.getId());
        assertThat(invoiceEs).isEqualToComparingFieldByField(testInvoice);
    }

    @Test
    @Transactional
    public void createInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice with an existing ID
        invoice.setId(1L);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setNumber(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPeriodStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setPeriodStartDate(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPeriodEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setPeriodEndDate(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIssueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setIssueDate(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setDueDate(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoices() throws Exception {
        // User Login
        userDTO = new UserDTO();
        userDTO.setLogin("test");
        userDTO.setEmail("test@localhost");
        userDTO.setFirstName("test");
        userDTO.setLastName("test");

        user = userService.registerUser(userDTO, "");
        userService.activateRegistration(user.getActivationKey());
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDTO.getLogin(), "");
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Set user reference
        invoice.getTeachingInstructor().setUser(user);

        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].periodStartDate").value(hasItem(DEFAULT_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodEndDate").value(hasItem(DEFAULT_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].totalCurrency").value(hasItem(DEFAULT_TOTAL_CURRENCY.toString())));
    }

    @Test
    @Transactional
    public void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.periodStartDate").value(DEFAULT_PERIOD_START_DATE.toString()))
            .andExpect(jsonPath("$.periodEndDate").value(DEFAULT_PERIOD_END_DATE.toString()))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.intValue()))
            .andExpect(jsonPath("$.totalCurrency").value(DEFAULT_TOTAL_CURRENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        invoiceSearchRepository.save(invoice);
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findOne(invoice.getId());
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .number(UPDATED_NUMBER)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodEndDate(UPDATED_PERIOD_END_DATE)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .totalCurrency(UPDATED_TOTAL_CURRENCY);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testInvoice.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
        assertThat(testInvoice.getPeriodEndDate()).isEqualTo(UPDATED_PERIOD_END_DATE);
        assertThat(testInvoice.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testInvoice.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testInvoice.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testInvoice.getTotalCurrency()).isEqualTo(UPDATED_TOTAL_CURRENCY);

        // Validate the Invoice in Elasticsearch
        Invoice invoiceEs = invoiceSearchRepository.findOne(testInvoice.getId());
        assertThat(invoiceEs).isEqualToComparingFieldByField(testInvoice);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        invoiceSearchRepository.save(invoice);
        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Get the invoice
        restInvoiceMockMvc.perform(delete("/api/invoices/{id}", invoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean invoiceExistsInEs = invoiceSearchRepository.exists(invoice.getId());
        assertThat(invoiceExistsInEs).isFalse();

        // Validate the database is empty
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        invoiceSearchRepository.save(invoice);

        // Search the invoice
        restInvoiceMockMvc.perform(get("/api/_search/invoices?query=id:" + invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].periodStartDate").value(hasItem(DEFAULT_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodEndDate").value(hasItem(DEFAULT_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].totalCurrency").value(hasItem(DEFAULT_TOTAL_CURRENCY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        Invoice invoice2 = new Invoice();
        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);
        invoice2.setId(2L);
        assertThat(invoice1).isNotEqualTo(invoice2);
        invoice1.setId(null);
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceDTO.class);
        InvoiceDTO invoiceDTO1 = new InvoiceDTO();
        invoiceDTO1.setId(1L);
        InvoiceDTO invoiceDTO2 = new InvoiceDTO();
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO2.setId(invoiceDTO1.getId());
        assertThat(invoiceDTO1).isEqualTo(invoiceDTO2);
        invoiceDTO2.setId(2L);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO1.setId(null);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceMapper.fromId(null)).isNull();
    }
}
