package com.sevago.mpc.service.impl;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.domain.Location;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.LocationRepository;
import com.sevago.mpc.repository.search.LocationSearchRepository;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.LocationService;
import com.sevago.mpc.service.UserService;
import com.sevago.mpc.service.dto.LocationDTO;
import com.sevago.mpc.service.dto.UserDTO;
import com.sevago.mpc.service.mapper.LocationMapper;
import com.sevago.mpc.web.rest.ActivityResourceIntTest;
import com.sevago.mpc.web.rest.LocationResourceIntTest;
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
public class LocationServiceImplTest {

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
    private LocationRepository locationRepository;

    @Autowired
    private LocationSearchRepository locationSearchRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationMapper locationMapper;

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
        locationRepository.deleteAll();
        userService.deleteUser(USER);
    }

    @Test
    public void shouldAuthenticateUser() {
        assertThat(SecurityUtils.getCurrentUserLogin()).isPresent();
    }

    @Test
    public void assertThatSaveCanSaveOnlyInMainRepo() {
        //given
        Location locationOne = LocationResourceIntTest.createEntity(em);
        locationOne.setUser(user);
        int sizeBeforeSave = locationRepository.findAll().size();

        //when
        LocationDTO savedLocationDTO = locationService.save(locationMapper.toDto(locationOne));
        List<Location> locationList = locationRepository.findAll();
        boolean locationExistsInSearchRepo = locationSearchRepository.exists(savedLocationDTO.getId());

        //then
        assertThat(locationList).hasSize(sizeBeforeSave + 1);
        assertThat(locationExistsInSearchRepo).isFalse();
    }

    @Test
    public void assertThatFindAllActivitiesCanFindByCurrentUser() throws Exception {
        //given
        Location locationOne = LocationResourceIntTest.createEntity(em);
        locationOne.setUser(user);
        locationRepository.saveAndFlush(locationOne);
        Location locationTwo = LocationResourceIntTest.createEntity(em);
        locationTwo.setDescription(NAME_TWO);
        locationRepository.saveAndFlush(locationTwo);

        //when
        List<LocationDTO> locationList = locationService.findAll();

        //then
        assertThat(locationList).isNotNull();
        assertThat(locationList.get(0).getDescription()).isEqualTo(locationOne.getDescription());
        assertThat(locationList.size()).isOne();
    }

    @Test
    public void assertThatDeleteCanNotDeleteInSearchRepo() {
        //given
        Location locationOne = LocationResourceIntTest.createEntity(em);
        locationOne.setUser(user);
        locationRepository.saveAndFlush(locationOne);
        locationSearchRepository.save(locationOne);
        int sizeBeforeDelete = locationRepository.findAll().size();

        //when
        locationService.delete(locationOne.getId());
        List<Location> locationList = locationRepository.findAll();
        boolean locationExistsInSearchRepo = locationSearchRepository.exists(locationOne.getId());

        //then
        assertThat(locationList).hasSize(sizeBeforeDelete - 1);
        assertThat(locationExistsInSearchRepo).isTrue();
    }
}
