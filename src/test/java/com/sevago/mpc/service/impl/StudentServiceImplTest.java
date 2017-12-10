package com.sevago.mpc.service.impl;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.domain.Student;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.StudentRepository;
import com.sevago.mpc.repository.search.StudentSearchRepository;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.StudentService;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.StudentDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.StudentMapper;
import com.sevago.mpc.web.rest.ActivityResourceIntTest;
import com.sevago.mpc.web.rest.StudentResourceIntTest;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
@Transactional
public class StudentServiceImplTest {

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
    private StudentRepository studentRepository;

    @Autowired
    private StudentSearchRepository studentSearchRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentMapper studentMapper;

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
        studentRepository.deleteAll();
        userService.deleteUser(USER);
    }

    @Test
    public void shouldAuthenticateUser() {
        assertThat(SecurityUtils.getCurrentUserLogin()).isPresent();
    }

    @Test
    public void assertThatSaveCanSaveOnlyInMainRepo() {
        //given
        Student studentOne = StudentResourceIntTest.createEntity(em);
        studentOne.setUser(user);
        int sizeBeforeSave = studentRepository.findAll().size();

        //when
        StudentDTO savedStudentDTO = studentService.save(studentMapper.toDto(studentOne));
        List<Student> studentList = studentRepository.findAll();
        boolean studentExistsInSearchRepo = studentSearchRepository.exists(savedStudentDTO.getId());

        //then
        assertThat(studentList).hasSize(sizeBeforeSave + 1);
        assertThat(studentExistsInSearchRepo).isFalse();
    }

    @Test
    public void assertThatFindAllActivitiesCanFindByCurrentUser() throws Exception {
        //given
        Student studentOne = StudentResourceIntTest.createEntity(em);
        studentOne.setUser(user);
        studentRepository.saveAndFlush(studentOne);
        Student studentTwo = StudentResourceIntTest.createEntity(em);
        studentTwo.setName(NAME_TWO);
        studentRepository.saveAndFlush(studentTwo);

        //when
        Page<StudentDTO> studentList = studentService.findAll(new PageRequest(0, 10));

        //then
        assertThat(studentList).isNotNull();
        assertThat(studentList.getContent().get(0).getName()).isEqualTo(studentOne.getName());
        assertThat(studentList.getContent().size()).isOne();
    }

    @Test
    public void assertThatDeleteCanNotDeleteInSearchRepo() {
        //given
        Student studentOne = StudentResourceIntTest.createEntity(em);
        studentOne.setUser(user);
        studentRepository.saveAndFlush(studentOne);
        studentSearchRepository.save(studentOne);
        int sizeBeforeDelete = studentRepository.findAll().size();

        //when
        studentService.delete(studentOne.getId());
        List<Student> studentList = studentRepository.findAll();
        boolean studentExistsInSearchRepo = studentSearchRepository.exists(studentOne.getId());

        //then
        assertThat(studentList).hasSize(sizeBeforeDelete - 1);
        assertThat(studentExistsInSearchRepo).isTrue();
    }
}
