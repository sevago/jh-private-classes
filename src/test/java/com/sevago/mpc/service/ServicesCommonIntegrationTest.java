package com.sevago.mpc.service;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.domain.Activity;
import com.sevago.mpc.domain.Instructor;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.ActivityRepository;
import com.sevago.mpc.repository.InstructorRepository;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.dto.ActivityDTO;
import com.sevago.mpc.service.dto.InstructorDTO;
import com.sevago.mpc.service.dto.UserDTO;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
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
public class ServicesCommonIntegrationTest {

    private static final String NAME_ONE = "Name One";
    private static final String NAME_TWO = "Name Two";
    private static final String USER = "test";

    private UserDTO userDTO;
    private User user;

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ActivityService activityService;

    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private InstructorService instructorService;



    @Before
    public void setUp() throws Exception {
        userDTO = new UserDTO();
        userDTO.setLogin(USER);
        userDTO.setEmail("test@localhost");
        userDTO.setFirstName(USER);
        userDTO.setLastName(USER);

        user = userService.registerUser(userDTO, "");
        userService.activateRegistration(user.getActivationKey());
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDTO.getLogin(), "");
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @After
    public void tearDown() throws Exception {
        activityRepository.deleteAll();
        instructorRepository.deleteAll();
        userService.deleteUser(USER);
    }

    @Test
    public void shouldAuthenticateUser() {
        assertThat(SecurityUtils.getCurrentUserLogin()).isPresent();
    }

    @Test
    @Transactional
    public void assertThatFindAllActivitiesCanFindByCurrentUser() throws Exception {
        //given
        Activity activityOne = new Activity();
        activityOne.setName(NAME_ONE);
        activityOne.setUser(user);
        activityRepository.saveAndFlush(activityOne);
        Activity activityTwo = new Activity();
        activityTwo.setName(NAME_TWO);
        activityRepository.saveAndFlush(activityTwo);

        //when
        List<ActivityDTO> activities = activityService.findAll();

        //then
        assertThat(activities).isNotNull();
        assertThat(activities.get(0).getName()).isEqualTo(activityOne.getName());
        assertThat(activities.size()).isOne();
    }

    @Test
    @Transactional
    public void assertThatFindAllInstructorsCanFindByCurrentUser() throws Exception {
        //given
        Instructor instructorOne = new Instructor();
        instructorOne.setName(NAME_ONE);
        instructorOne.setUser(user);
        instructorRepository.saveAndFlush(instructorOne);
        Instructor instructorTwo = new Instructor();
        instructorTwo.setName(NAME_TWO);
        instructorRepository.saveAndFlush(instructorTwo);

        //when
        List<InstructorDTO> instructors = instructorService.findAll();

        //then
        assertThat(instructors).isNotNull();
        assertThat(instructors.get(0).getName()).isEqualTo(instructorOne.getName());
        assertThat(instructors.size()).isOne();
    }

}
