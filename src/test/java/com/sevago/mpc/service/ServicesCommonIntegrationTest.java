package com.sevago.mpc.service;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.domain.*;
import com.sevago.mpc.repository.ActivityRepository;
import com.sevago.mpc.repository.InstructorRepository;
import com.sevago.mpc.repository.InvoiceRepository;
import com.sevago.mpc.repository.LessonRepository;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.dto.*;
import com.sevago.mpc.web.rest.ActivityResourceIntTest;
import com.sevago.mpc.web.rest.InstructorResourceIntTest;
import com.sevago.mpc.web.rest.InvoiceResourceIntTest;
import com.sevago.mpc.web.rest.LessonResourceIntTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
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
    private EntityManager em;

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

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private LessonService lessonService;


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
        invoiceRepository.deleteAll();
        lessonRepository.deleteAll();
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
        Activity activityOne = ActivityResourceIntTest.createEntity(em);
        activityOne.setUser(user);
        activityRepository.saveAndFlush(activityOne);
        Activity activityTwo = ActivityResourceIntTest.createEntity(em);
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
        Instructor instructorOne = InstructorResourceIntTest.createEntity(em);
        instructorOne.setUser(user);
        instructorRepository.saveAndFlush(instructorOne);
        Instructor instructorTwo = InstructorResourceIntTest.createEntity(em);
        instructorTwo.setName(NAME_TWO);
        instructorRepository.saveAndFlush(instructorTwo);

        //when
        List<InstructorDTO> instructors = instructorService.findAll();

        //then
        assertThat(instructors).isNotNull();
        assertThat(instructors.get(0).getName()).isEqualTo(instructorOne.getName());
        assertThat(instructors.size()).isOne();
    }

    @Test
    @Transactional
    public void assertThatFindAllInvoicesCanFindByCurrentUser() throws Exception {
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
    @Transactional
    public void assertThatFindAllLessonsCanFindByCurrentUser() throws Exception {
        //given
        Lesson lessonOne = LessonResourceIntTest.createEntity(em);
        lessonOne.getTeachingInstructor().setUser(user);
        lessonRepository.saveAndFlush(lessonOne);
        Lesson lessonTwo = LessonResourceIntTest.createEntity(em);
        lessonTwo.setDuration(BigDecimal.valueOf(2));
        lessonRepository.saveAndFlush(lessonTwo);

        //when
        Page<LessonDTO> lessons = lessonService.findAll(new PageRequest(0, 10));

        //then
        assertThat(lessons).isNotNull();
        assertThat(lessons.getContent().get(0).getDuration()).isEqualTo(lessonOne.getDuration());
        assertThat(lessons.getContent().size()).isOne();
    }
}
