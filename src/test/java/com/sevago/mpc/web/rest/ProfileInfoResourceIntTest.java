package com.sevago.mpc.web.rest;

import com.sevago.mpc.config.ApplicationProperties;
import io.github.jhipster.config.JHipsterProperties;
import com.sevago.mpc.PrivateclassesApp;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ProfileInfoResource REST controller.
 *
 * @see ProfileInfoResource
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
public class ProfileInfoResourceIntTest {

    @Mock
    private Environment environment;

    @Mock
    private JHipsterProperties jHipsterProperties;

    @Mock
    private ApplicationProperties applicationProperties;

    private MockMvc restProfileMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        String mockProfile[] = { "test" };
        JHipsterProperties.Ribbon ribbon = new JHipsterProperties.Ribbon();
        ribbon.setDisplayOnActiveProfiles(mockProfile);
        when(jHipsterProperties.getRibbon()).thenReturn(ribbon);

        String activeProfiles[] = {"test"};

        when(environment.getDefaultProfiles()).thenReturn(activeProfiles);
        when(environment.getActiveProfiles()).thenReturn(activeProfiles);

        ApplicationProperties.Elasticsearch elasticsearch = new ApplicationProperties.Elasticsearch();
        when(applicationProperties.getElasticsearch()).thenReturn(elasticsearch);

        ProfileInfoResource profileInfoResource = new ProfileInfoResource(environment, jHipsterProperties, applicationProperties);
        this.restProfileMockMvc = MockMvcBuilders
            .standaloneSetup(profileInfoResource)
            .build();
    }

    @Test
    public void getProfileInfoWithRibbon() throws Exception {
        restProfileMockMvc.perform(get("/api/profile-info"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void getProfileInfoWithoutRibbon() throws Exception {
        JHipsterProperties.Ribbon ribbon = new JHipsterProperties.Ribbon();
        ribbon.setDisplayOnActiveProfiles(null);
        when(jHipsterProperties.getRibbon()).thenReturn(ribbon);

        restProfileMockMvc.perform(get("/api/profile-info"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void getProfileInfoWithoutActiveProfiles() throws Exception {
        String emptyProfile[] = {};
        when(environment.getDefaultProfiles()).thenReturn(emptyProfile);
        when(environment.getActiveProfiles()).thenReturn(emptyProfile);

        restProfileMockMvc.perform(get("/api/profile-info"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void getProfileInfoWithElasticsearchEnabledFlag() throws Exception {
        //given
        ApplicationProperties.Elasticsearch elasticsearch = new ApplicationProperties.Elasticsearch();

        //when
        when(applicationProperties.getElasticsearch()).thenReturn(elasticsearch);

        //then
        restProfileMockMvc.perform(get("/api/profile-info"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.elasticsearchEnabled").value(true));
    }
}
