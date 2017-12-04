package com.sevago.mpc.service;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.domain.Activity;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.ActivityRepository;
import com.sevago.mpc.service.dto.ActivityDTO;
import com.sevago.mpc.service.dto.UserDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
@Transactional
public class ActivityServiceIntTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    private UserDTO userDTO;

    private User user;

    @Before
    public void setUp() throws Exception {
        userDTO = new UserDTO();
        userDTO.setLogin("test");
        userDTO.setEmail("test@localhost");
        userDTO.setFirstName("test");
        userDTO.setLastName("test");
    }

    @After
    public void tearDown() throws Exception {
        activityRepository.deleteAll();
    }

    @Test
    @Transactional
    public void assertThatFindAllCanFindByCurrentUser() throws Exception {
        //given
        user = userService.registerUser(userDTO, "");
        userService.activateRegistration(user.getActivationKey());
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDTO.getLogin(), "");
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Activity activity1 = new Activity();
        activity1.setName("activity1");
        activity1.setDescription("activity1");
        activity1.setUser(user);
        activityRepository.saveAndFlush(activity1);
        Activity activity2 = new Activity();
        activity2.setName("activity2");
        activity2.setDescription("activity2");
        activityRepository.saveAndFlush(activity2);

        //when
        List<ActivityDTO> activities = activityService.findAll();

        //then
        assertThat(activities).isNotNull();
        assertThat(activities.get(0).getName()).isEqualTo(activities.get(0).getName());
        assertThat(activities.size()).isOne();
    }
}
