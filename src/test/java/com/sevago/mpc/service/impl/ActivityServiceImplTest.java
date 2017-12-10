package com.sevago.mpc.service.impl;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.domain.Activity;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.ActivityRepository;
import com.sevago.mpc.repository.search.ActivitySearchRepository;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.ActivityService;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.ActivityDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.ActivityMapper;
import com.sevago.mpc.web.rest.ActivityResourceIntTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
@Transactional
public class ActivityServiceImplTest {

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
    private ActivityRepository activityRepository;

    @Autowired
    private ActivitySearchRepository activitySearchRepository;

    @SpyBean
    private ActivityService activityService;

    @Autowired
    private ActivityMapper activityMapper;

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
        activityRepository.deleteAll();
        userService.deleteUser(USER);
    }

    @Test
    public void shouldAuthenticateUser() {
        assertThat(SecurityUtils.getCurrentUserLogin()).isPresent();
    }

    @Test
    public void assertThatSaveCanSaveOnlyInMainRepo() {
        //given
        Activity activityOne = ActivityResourceIntTest.createEntity(em);
        activityOne.setUser(user);
        int sizeBeforeSave = activityRepository.findAll().size();

        //when
        ActivityDTO savedActivityDTO = activityService.save(activityMapper.toDto(activityOne));
        List<Activity> activityList = activityRepository.findAll();
        boolean activityExistsInSearchRepo = activitySearchRepository.exists(savedActivityDTO.getId());

        //then
        assertThat(activityList).hasSize(sizeBeforeSave + 1);
        assertThat(activityExistsInSearchRepo).isFalse();
    }

    @Test
    public void assertThatFindAllActivitiesCanFindByCurrentUser() throws Exception {
        //given
        Activity activityOne = ActivityResourceIntTest.createEntity(em);
        activityOne.setUser(user);
        activityRepository.saveAndFlush(activityOne);
        Activity activityTwo = ActivityResourceIntTest.createEntity(em);
        activityTwo.setName(NAME_TWO);
        activityRepository.saveAndFlush(activityTwo);

        //when
        List<ActivityDTO> activityList = activityService.findAll();

        //then
        assertThat(activityList).isNotNull();
        assertThat(activityList.get(0).getName()).isEqualTo(activityOne.getName());
        assertThat(activityList.size()).isOne();
    }

    @Test
    public void assertThatDeleteCanNotDeleteInSearchRepo() {
        //given
        Activity activityOne = ActivityResourceIntTest.createEntity(em);
        activityOne.setUser(user);
        activityRepository.saveAndFlush(activityOne);
        activitySearchRepository.save(activityOne);
        int sizeBeforeDelete = activityRepository.findAll().size();

        //when
        activityService.delete(activityOne.getId());
        List<Activity> activityList = activityRepository.findAll();
        boolean activityExistsInSearchRepo = activitySearchRepository.exists(activityOne.getId());

        //then
        verify(activityService, never()).findOne(anyLong());
        verify(activityService, atLeastOnce()).delete(anyLong());
        assertThat(activityList).hasSize(sizeBeforeDelete - 1);
        assertThat(activityExistsInSearchRepo).isTrue();
    }
}
