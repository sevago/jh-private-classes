package com.sevago.mpc.service.impl;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.domain.Preferences;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.PreferencesRepository;
import com.sevago.mpc.repository.search.PreferencesSearchRepository;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.PreferencesService;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.PreferencesDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.PreferencesMapper;
import com.sevago.mpc.web.rest.ActivityResourceIntTest;
import com.sevago.mpc.web.rest.PreferencesResourceIntTest;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
@Transactional
public class PreferencesServiceImplTest {

    private static final String USER = "test";

    private User user;

    @Autowired
    private EntityManager em;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Autowired
    private PreferencesSearchRepository preferencesSearchRepository;

    @Autowired
    private PreferencesService preferencesService;

    @Autowired
    private PreferencesMapper preferencesMapper;

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
        preferencesRepository.deleteAll();
        userService.deleteUser(USER);
    }

    @Test
    public void shouldAuthenticateUser() {
        assertThat(SecurityUtils.getCurrentUserLogin()).isPresent();
    }

    @Test
    public void assertThatSaveCanSaveOnlyInMainRepo() {
        //given
        Preferences preferencesOne = PreferencesResourceIntTest.createEntity(em);
        preferencesOne.setUser(user);
        int sizeBeforeSave = preferencesRepository.findAll().size();

        //when
        PreferencesDTO savedPreferencesDTO = preferencesService.save(preferencesMapper.toDto(preferencesOne));
        List<Preferences> preferencesList = preferencesRepository.findAll();
        boolean preferencesExistsInSearchRepo = preferencesSearchRepository.exists(savedPreferencesDTO.getId());

        //then
        assertThat(preferencesList).hasSize(sizeBeforeSave + 1);
        assertThat(preferencesExistsInSearchRepo).isFalse();
    }

    @Test
    public void assertThatFindAllActivitiesCanFindByCurrentUser() throws Exception {
        //given
        Preferences preferencesOne = PreferencesResourceIntTest.createEntity(em);
        preferencesOne.setUser(user);
        preferencesRepository.saveAndFlush(preferencesOne);
        Preferences preferencesTwo = PreferencesResourceIntTest.createEntity(em);
        preferencesRepository.saveAndFlush(preferencesTwo);

        //when
        List<PreferencesDTO> preferencesList = preferencesService.findAll();

        //then
        assertThat(preferencesList).isNotNull();
        assertThat(preferencesList.get(0).getId()).isEqualTo(preferencesOne.getId());
        assertThat(preferencesList.size()).isOne();
    }

    @Test
    public void assertThatDeleteCanNotDeleteInSearchRepo() {
        //given
        Preferences preferencesOne = PreferencesResourceIntTest.createEntity(em);
        preferencesOne.setUser(user);
        preferencesRepository.saveAndFlush(preferencesOne);
        preferencesSearchRepository.save(preferencesOne);
        int sizeBeforeDelete = preferencesRepository.findAll().size();

        //when
        preferencesService.delete(preferencesOne.getId());
        List<Preferences> preferencesList = preferencesRepository.findAll();
        boolean preferencesExistsInSearchRepo = preferencesSearchRepository.exists(preferencesOne.getId());

        //then
        assertThat(preferencesList).hasSize(sizeBeforeDelete - 1);
        assertThat(preferencesExistsInSearchRepo).isTrue();
    }
}
