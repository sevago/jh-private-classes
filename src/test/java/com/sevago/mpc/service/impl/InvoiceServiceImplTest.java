package com.sevago.mpc.service.impl;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.domain.Invoice;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.InvoiceRepository;
import com.sevago.mpc.repository.search.InvoiceSearchRepository;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.InvoiceService;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.InvoiceDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.InvoiceMapper;
import com.sevago.mpc.web.rest.ActivityResourceIntTest;
import com.sevago.mpc.web.rest.InvoiceResourceIntTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
@Transactional
public class InvoiceServiceImplTest {

    private static final String NAME_ONE = "Name One";
    private static final String NAME_TWO = "Name Two";
    private static final String USER = "test";

    private UserDTO userDTO;
    private User user;

    @Autowired
    private EntityManager em;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceSearchRepository invoiceSearchRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Before
    public void setUp() throws Exception {
        applicationProperties.getElasticsearch().setEnabled(false);
        user = ActivityResourceIntTest.userAuthentication(userService, authenticationManager);
    }

    @After
    public void tearDown() throws Exception {
        applicationProperties.getElasticsearch().setEnabled(true);
        invoiceRepository.deleteAll();
        userService.deleteUser(USER);
    }

    @Test
    public void shouldAuthenticateUser() {
        assertThat(SecurityUtils.getCurrentUserLogin()).isPresent();
    }

    @Test
    public void assertThatSaveCanSaveOnlyInMainRepo() {
        //given
        Invoice invoiceOne = InvoiceResourceIntTest.createEntity(em);
        invoiceOne.getTeachingInstructor().setUser(user);
        int sizeBeforeSave = invoiceRepository.findAll().size();

        //when
        InvoiceDTO savedInvoiceDTO = invoiceService.save(invoiceMapper.toDto(invoiceOne));
        List<Invoice> invoiceList = invoiceRepository.findAll();
        boolean invoiceExistsInSearchRepo = invoiceSearchRepository.exists(savedInvoiceDTO.getId());

        //then
        assertThat(invoiceList).hasSize(sizeBeforeSave + 1);
        assertThat(invoiceExistsInSearchRepo).isFalse();
    }

    @Test
    public void assertThatFindAllActivitiesCanFindByCurrentUser() throws Exception {
        //given
        Invoice invoiceOne = InvoiceResourceIntTest.createEntity(em);
        invoiceOne.getTeachingInstructor().setUser(user);
        invoiceRepository.saveAndFlush(invoiceOne);
        Invoice invoiceTwo = InvoiceResourceIntTest.createEntity(em);
        invoiceTwo.setNumber(2);
        invoiceRepository.saveAndFlush(invoiceTwo);

        //when
        Page<InvoiceDTO> invoices = invoiceService.findAll(new PageRequest(0, 10));

        //then
        assertThat(invoices).isNotNull();
        assertThat(invoices.getContent().get(0).getNumber()).isEqualTo(invoiceOne.getNumber());
        assertThat(invoices.getContent().size()).isOne();
    }

    @Test
    public void assertThatDeleteCanNotDeleteInSearchRepo() {
        //given
        Invoice invoiceOne = InvoiceResourceIntTest.createEntity(em);
        invoiceOne.getTeachingInstructor().setUser(user);
        invoiceRepository.saveAndFlush(invoiceOne);
        invoiceSearchRepository.save(invoiceOne);
        int sizeBeforeDelete = invoiceRepository.findAll().size();

        //when
        invoiceService.delete(invoiceOne.getId());
        List<Invoice> invoiceList = invoiceRepository.findAll();
        boolean invoiceExistsInSearchRepo = invoiceSearchRepository.exists(invoiceOne.getId());

        //then
        assertThat(invoiceList).hasSize(sizeBeforeDelete - 1);
        assertThat(invoiceExistsInSearchRepo).isTrue();
    }
}
