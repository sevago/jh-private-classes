package com.sevago.mpc.service.impl;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.domain.Lesson;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.LessonRepository;
import com.sevago.mpc.repository.search.LessonSearchRepository;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.LessonService;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.LessonDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.LessonMapper;
import com.sevago.mpc.web.rest.ActivityResourceIntTest;
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
public class LessonServiceImplTest {

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
    private LessonRepository lessonRepository;

    @Autowired
    private LessonSearchRepository lessonSearchRepository;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonMapper lessonMapper;

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
        lessonRepository.deleteAll();
        userService.deleteUser(USER);
    }

    @Test
    public void shouldAuthenticateUser() {
        assertThat(SecurityUtils.getCurrentUserLogin()).isPresent();
    }

    @Test
    public void assertThatSaveCanSaveOnlyInMainRepo() {
        //given
        Lesson lessonOne = LessonResourceIntTest.createEntity(em);
        lessonOne.getTeachingInstructor().setUser(user);
        int sizeBeforeSave = lessonRepository.findAll().size();

        //when
        LessonDTO savedLessonDTO = lessonService.save(lessonMapper.toDto(lessonOne));
        List<Lesson> lessonList = lessonRepository.findAll();
        boolean lessonExistsInSearchRepo = lessonSearchRepository.exists(savedLessonDTO.getId());

        //then
        assertThat(lessonList).hasSize(sizeBeforeSave + 1);
        assertThat(lessonExistsInSearchRepo).isFalse();
    }

    @Test
    public void assertThatFindAllActivitiesCanFindByCurrentUser() throws Exception {
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

    @Test
    public void assertThatDeleteCanNotDeleteInSearchRepo() {
        //given
        Lesson lessonOne = LessonResourceIntTest.createEntity(em);
        lessonOne.getTeachingInstructor().setUser(user);
        lessonRepository.saveAndFlush(lessonOne);
        lessonSearchRepository.save(lessonOne);
        int sizeBeforeDelete = lessonRepository.findAll().size();

        //when
        lessonService.delete(lessonOne.getId());
        List<Lesson> lessonList = lessonRepository.findAll();
        boolean lessonExistsInSearchRepo = lessonSearchRepository.exists(lessonOne.getId());

        //then
        assertThat(lessonList).hasSize(sizeBeforeDelete - 1);
        assertThat(lessonExistsInSearchRepo).isTrue();
    }
}
