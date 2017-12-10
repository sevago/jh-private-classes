package com.sevago.mpc.service.impl;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.domain.Instructor;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.InstructorRepository;
import com.sevago.mpc.repository.search.InstructorSearchRepository;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.InstructorService;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.InstructorDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.InstructorMapper;
import com.sevago.mpc.web.rest.ActivityResourceIntTest;
import com.sevago.mpc.web.rest.InstructorResourceIntTest;
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
public class InstructorServiceImplTest {

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
    private InstructorRepository instructorRepository;

    @Autowired
    private InstructorSearchRepository instructorSearchRepository;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private InstructorMapper instructorMapper;

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
        instructorRepository.deleteAll();
        userService.deleteUser(USER);
    }

    @Test
    public void shouldAuthenticateUser() {
        assertThat(SecurityUtils.getCurrentUserLogin()).isPresent();
    }

    @Test
    public void assertThatSaveCanSaveOnlyInMainRepo() {
        //given
        Instructor instructorOne = InstructorResourceIntTest.createEntity(em);
        instructorOne.setUser(user);
        int sizeBeforeSave = instructorRepository.findAll().size();

        //when
        InstructorDTO savedInstructorDTO = instructorService.save(instructorMapper.toDto(instructorOne));
        List<Instructor> instructorList = instructorRepository.findAll();
        boolean instructorExistsInSearchRepo = instructorSearchRepository.exists(savedInstructorDTO.getId());

        //then
        assertThat(instructorList).hasSize(sizeBeforeSave + 1);
        assertThat(instructorExistsInSearchRepo).isFalse();
    }

    @Test
    public void assertThatFindAllActivitiesCanFindByCurrentUser() throws Exception {
        //given
        Instructor instructorOne = InstructorResourceIntTest.createEntity(em);
        instructorOne.setUser(user);
        instructorRepository.saveAndFlush(instructorOne);
        Instructor instructorTwo = InstructorResourceIntTest.createEntity(em);
        instructorTwo.setName(NAME_TWO);
        instructorRepository.saveAndFlush(instructorTwo);

        //when
        List<InstructorDTO> instructorList = instructorService.findAll();

        //then
        assertThat(instructorList).isNotNull();
        assertThat(instructorList.get(0).getName()).isEqualTo(instructorOne.getName());
        assertThat(instructorList.size()).isOne();
    }

    @Test
    public void assertThatDeleteCanNotDeleteInSearchRepo() {
        //given
        Instructor instructorOne = InstructorResourceIntTest.createEntity(em);
        instructorOne.setUser(user);
        instructorRepository.saveAndFlush(instructorOne);
        instructorSearchRepository.save(instructorOne);
        int sizeBeforeDelete = instructorRepository.findAll().size();

        //when
        instructorService.delete(instructorOne.getId());
        List<Instructor> instructorList = instructorRepository.findAll();
        boolean instructorExistsInSearchRepo = instructorSearchRepository.exists(instructorOne.getId());

        //then
        assertThat(instructorList).hasSize(sizeBeforeDelete - 1);
        assertThat(instructorExistsInSearchRepo).isTrue();
    }
}
