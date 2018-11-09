package net.jianbo.cmdb.web.rest;

import net.jianbo.cmdb.MyCmdbApp;

import net.jianbo.cmdb.domain.Application;
import net.jianbo.cmdb.domain.ComponentEntity;
import net.jianbo.cmdb.repository.ApplicationRepository;
import net.jianbo.cmdb.service.ApplicationService;
import net.jianbo.cmdb.web.rest.errors.ExceptionTranslator;
import net.jianbo.cmdb.service.dto.ApplicationCriteria;
import net.jianbo.cmdb.service.ApplicationQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static net.jianbo.cmdb.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ApplicationResource REST controller.
 *
 * @see ApplicationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyCmdbApp.class)
public class ApplicationResourceIntTest {

    private static final String DEFAULT_APP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENVIRONMENT = "AAAAAAAAAA";
    private static final String UPDATED_ENVIRONMENT = "BBBBBBBBBB";

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationQueryService applicationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplicationMockMvc;

    private Application application;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationResource applicationResource = new ApplicationResource(applicationService, applicationQueryService);
        this.restApplicationMockMvc = MockMvcBuilders.standaloneSetup(applicationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createEntity(EntityManager em) {
        Application application = new Application()
            .appName(DEFAULT_APP_NAME)
            .environment(DEFAULT_ENVIRONMENT);
        // Add required entity
        ComponentEntity componentEntity = ComponentEntityResourceIntTest.createEntity(em);
        em.persist(componentEntity);
        em.flush();
        application.getComponents().add(componentEntity);
        return application;
    }

    @Before
    public void initTest() {
        application = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // Create the Application
        restApplicationMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getAppName()).isEqualTo(DEFAULT_APP_NAME);
        assertThat(testApplication.getEnvironment()).isEqualTo(DEFAULT_ENVIRONMENT);
    }

    @Test
    @Transactional
    public void createApplicationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // Create the Application with an existing ID
        application.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAppNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setAppName(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnvironmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setEnvironment(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList
        restApplicationMockMvc.perform(get("/api/applications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
            .andExpect(jsonPath("$.[*].appName").value(hasItem(DEFAULT_APP_NAME.toString())))
            .andExpect(jsonPath("$.[*].environment").value(hasItem(DEFAULT_ENVIRONMENT.toString())));
    }
    
    @Test
    @Transactional
    public void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(application.getId().intValue()))
            .andExpect(jsonPath("$.appName").value(DEFAULT_APP_NAME.toString()))
            .andExpect(jsonPath("$.environment").value(DEFAULT_ENVIRONMENT.toString()));
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppNameIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where appName equals to DEFAULT_APP_NAME
        defaultApplicationShouldBeFound("appName.equals=" + DEFAULT_APP_NAME);

        // Get all the applicationList where appName equals to UPDATED_APP_NAME
        defaultApplicationShouldNotBeFound("appName.equals=" + UPDATED_APP_NAME);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppNameIsInShouldWork() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where appName in DEFAULT_APP_NAME or UPDATED_APP_NAME
        defaultApplicationShouldBeFound("appName.in=" + DEFAULT_APP_NAME + "," + UPDATED_APP_NAME);

        // Get all the applicationList where appName equals to UPDATED_APP_NAME
        defaultApplicationShouldNotBeFound("appName.in=" + UPDATED_APP_NAME);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where appName is not null
        defaultApplicationShouldBeFound("appName.specified=true");

        // Get all the applicationList where appName is null
        defaultApplicationShouldNotBeFound("appName.specified=false");
    }

    @Test
    @Transactional
    public void getAllApplicationsByEnvironmentIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where environment equals to DEFAULT_ENVIRONMENT
        defaultApplicationShouldBeFound("environment.equals=" + DEFAULT_ENVIRONMENT);

        // Get all the applicationList where environment equals to UPDATED_ENVIRONMENT
        defaultApplicationShouldNotBeFound("environment.equals=" + UPDATED_ENVIRONMENT);
    }

    @Test
    @Transactional
    public void getAllApplicationsByEnvironmentIsInShouldWork() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where environment in DEFAULT_ENVIRONMENT or UPDATED_ENVIRONMENT
        defaultApplicationShouldBeFound("environment.in=" + DEFAULT_ENVIRONMENT + "," + UPDATED_ENVIRONMENT);

        // Get all the applicationList where environment equals to UPDATED_ENVIRONMENT
        defaultApplicationShouldNotBeFound("environment.in=" + UPDATED_ENVIRONMENT);
    }

    @Test
    @Transactional
    public void getAllApplicationsByEnvironmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where environment is not null
        defaultApplicationShouldBeFound("environment.specified=true");

        // Get all the applicationList where environment is null
        defaultApplicationShouldNotBeFound("environment.specified=false");
    }

    @Test
    @Transactional
    public void getAllApplicationsByComponentsIsEqualToSomething() throws Exception {
        // Initialize the database
        ComponentEntity components = ComponentEntityResourceIntTest.createEntity(em);
        em.persist(components);
        em.flush();
        application.addComponents(components);
        applicationRepository.saveAndFlush(application);
        Long componentsId = components.getId();

        // Get all the applicationList where components equals to componentsId
        defaultApplicationShouldBeFound("componentsId.equals=" + componentsId);

        // Get all the applicationList where components equals to componentsId + 1
        defaultApplicationShouldNotBeFound("componentsId.equals=" + (componentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultApplicationShouldBeFound(String filter) throws Exception {
        restApplicationMockMvc.perform(get("/api/applications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
            .andExpect(jsonPath("$.[*].appName").value(hasItem(DEFAULT_APP_NAME.toString())))
            .andExpect(jsonPath("$.[*].environment").value(hasItem(DEFAULT_ENVIRONMENT.toString())));

        // Check, that the count call also returns 1
        restApplicationMockMvc.perform(get("/api/applications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultApplicationShouldNotBeFound(String filter) throws Exception {
        restApplicationMockMvc.perform(get("/api/applications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicationMockMvc.perform(get("/api/applications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplication() throws Exception {
        // Initialize the database
        applicationService.save(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = applicationRepository.findById(application.getId()).get();
        // Disconnect from session so that the updates on updatedApplication are not directly saved in db
        em.detach(updatedApplication);
        updatedApplication
            .appName(UPDATED_APP_NAME)
            .environment(UPDATED_ENVIRONMENT);

        restApplicationMockMvc.perform(put("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplication)))
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getAppName()).isEqualTo(UPDATED_APP_NAME);
        assertThat(testApplication.getEnvironment()).isEqualTo(UPDATED_ENVIRONMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Create the Application

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc.perform(put("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApplication() throws Exception {
        // Initialize the database
        applicationService.save(application);

        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Get the application
        restApplicationMockMvc.perform(delete("/api/applications/{id}", application.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Application.class);
        Application application1 = new Application();
        application1.setId(1L);
        Application application2 = new Application();
        application2.setId(application1.getId());
        assertThat(application1).isEqualTo(application2);
        application2.setId(2L);
        assertThat(application1).isNotEqualTo(application2);
        application1.setId(null);
        assertThat(application1).isNotEqualTo(application2);
    }
}
