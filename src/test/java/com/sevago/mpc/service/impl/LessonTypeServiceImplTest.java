package com.sevago.mpc.service.impl;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.domain.LessonType;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.LessonTypeRepository;
import com.sevago.mpc.repository.search.LessonTypeSearchRepository;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.LessonTypeService;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.LessonTypeDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.LessonTypeMapper;
import com.sevago.mpc.web.rest.ActivityResourceIntTest;
import com.sevago.mpc.web.rest.LessonTypeResourceIntTest;
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
public class LessonTypeServiceImplTest {

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
    private LessonTypeRepository lessonTypeRepository;

    @Autowired
    private LessonTypeSearchRepository lessonTypeSearchRepository;

    @Autowired
    private LessonTypeService lessonTypeService;

    @Autowired
    private LessonTypeMapper lessonTypeMapper;

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
        lessonTypeRepository.deleteAll();
        userService.deleteUser(USER);
    }

    @Test
    public void shouldAuthenticateUser() {
        assertThat(SecurityUtils.getCurrentUserLogin()).isPresent();
    }

    @Test
    public void assertThatSaveCanSaveOnlyInMainRepo() {
        //given
        LessonType lessonTypeOne = LessonTypeResourceIntTest.createEntity(em);
        lessonTypeOne.setUser(user);
        int sizeBeforeSave = lessonTypeRepository.findAll().size();

        //when
        LessonTypeDTO savedLessonTypeDTO = lessonTypeService.save(lessonTypeMapper.toDto(lessonTypeOne));
        List<LessonType> lessonTypeList = lessonTypeRepository.findAll();
        boolean lessonTypeExistsInSearchRepo = lessonTypeSearchRepository.exists(savedLessonTypeDTO.getId());

        //then
        assertThat(lessonTypeList).hasSize(sizeBeforeSave + 1);
        assertThat(lessonTypeExistsInSearchRepo).isFalse();
    }

    @Test
    public void assertThatFindAllActivitiesCanFindByCurrentUser() throws Exception {
        //given
        LessonType lessonTypeOne = LessonTypeResourceIntTest.createEntity(em);
        lessonTypeOne.setUser(user);
        lessonTypeRepository.saveAndFlush(lessonTypeOne);
        LessonType lessonTypeTwo = LessonTypeResourceIntTest.createEntity(em);
        lessonTypeTwo.setDescription(NAME_TWO);
        lessonTypeRepository.saveAndFlush(lessonTypeTwo);

        //when
        List<LessonTypeDTO> lessonTypeList = lessonTypeService.findAll();

        //then
        assertThat(lessonTypeList).isNotNull();
        assertThat(lessonTypeList.get(0).getDescription()).isEqualTo(lessonTypeOne.getDescription());
        assertThat(lessonTypeList.size()).isOne();
    }

    @Test
    public void assertThatDeleteCanNotDeleteInSearchRepo() {
        //given
        LessonType lessonTypeOne = LessonTypeResourceIntTest.createEntity(em);
        lessonTypeOne.setUser(user);
        lessonTypeRepository.saveAndFlush(lessonTypeOne);
        lessonTypeSearchRepository.save(lessonTypeOne);
        int sizeBeforeDelete = lessonTypeRepository.findAll().size();

        //when
        lessonTypeService.delete(lessonTypeOne.getId());
        List<LessonType> lessonTypeList = lessonTypeRepository.findAll();
        boolean lessonTypeExistsInSearchRepo = lessonTypeSearchRepository.exists(lessonTypeOne.getId());

        //then
        assertThat(lessonTypeList).hasSize(sizeBeforeDelete - 1);
        assertThat(lessonTypeExistsInSearchRepo).isTrue();
    }
}
