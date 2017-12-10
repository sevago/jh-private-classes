package com.sevago.mpc.service.impl;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.domain.Rate;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.RateRepository;
import com.sevago.mpc.repository.search.RateSearchRepository;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.RateService;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.RateDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.RateMapper;
import com.sevago.mpc.web.rest.ActivityResourceIntTest;
import com.sevago.mpc.web.rest.RateResourceIntTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
@Transactional
public class RateServiceImplTest {

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
    private RateRepository rateRepository;

    @Autowired
    private RateSearchRepository rateSearchRepository;

    @Autowired
    private RateService rateService;

    @Autowired
    private RateMapper rateMapper;

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
        rateRepository.deleteAll();
        userService.deleteUser(USER);
    }

    @Test
    public void shouldAuthenticateUser() {
        assertThat(SecurityUtils.getCurrentUserLogin()).isPresent();
    }

    @Test
    public void assertThatSaveCanSaveOnlyInMainRepo() {
        //given
        Rate rateOne = RateResourceIntTest.createEntity(em);
        rateOne.setUser(user);
        int sizeBeforeSave = rateRepository.findAll().size();

        //when
        RateDTO savedRateDTO = rateService.save(rateMapper.toDto(rateOne));
        List<Rate> rateList = rateRepository.findAll();
        boolean rateExistsInSearchRepo = rateSearchRepository.exists(savedRateDTO.getId());

        //then
        assertThat(rateList).hasSize(sizeBeforeSave + 1);
        assertThat(rateExistsInSearchRepo).isFalse();
    }

    @Test
    public void assertThatFindAllActivitiesCanFindByCurrentUser() throws Exception {
        //given
        Rate rateOne = RateResourceIntTest.createEntity(em);
        rateOne.setUser(user);
        rateRepository.saveAndFlush(rateOne);
        Rate rateTwo = RateResourceIntTest.createEntity(em);
        rateTwo.setDescription(NAME_TWO);
        rateRepository.saveAndFlush(rateTwo);

        //when
        List<RateDTO> rateList = rateService.findAll();

        //then
        assertThat(rateList).isNotNull();
        assertThat(rateList.get(0).getDescription()).isEqualTo(rateOne.getDescription());
        assertThat(rateList.size()).isOne();
    }

    @Test
    public void assertThatDeleteCanNotDeleteInSearchRepo() {
        //given
        Rate rateOne = RateResourceIntTest.createEntity(em);
        rateOne.setUser(user);
        rateRepository.saveAndFlush(rateOne);
        rateSearchRepository.save(rateOne);
        int sizeBeforeDelete = rateRepository.findAll().size();

        //when
        rateService.delete(rateOne.getId());
        List<Rate> rateList = rateRepository.findAll();
        boolean rateExistsInSearchRepo = rateSearchRepository.exists(rateOne.getId());

        //then
        assertThat(rateList).hasSize(sizeBeforeDelete - 1);
        assertThat(rateExistsInSearchRepo).isTrue();
    }
}
