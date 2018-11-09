package net.jianbo.cmdb.web.rest;

import net.jianbo.cmdb.MyCmdbApp;

import net.jianbo.cmdb.domain.ComponentEntity;
import net.jianbo.cmdb.domain.Application;
import net.jianbo.cmdb.domain.Server;
import net.jianbo.cmdb.repository.ComponentEntityRepository;
import net.jianbo.cmdb.service.ComponentEntityService;
import net.jianbo.cmdb.web.rest.errors.ExceptionTranslator;
import net.jianbo.cmdb.service.dto.ComponentEntityCriteria;
import net.jianbo.cmdb.service.ComponentEntityQueryService;

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

import net.jianbo.cmdb.domain.enumeration.ComponentType;
/**
 * Test class for the ComponentEntityResource REST controller.
 *
 * @see ComponentEntityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyCmdbApp.class)
public class ComponentEntityResourceIntTest {

    private static final String DEFAULT_COM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COM_NAME = "BBBBBBBBBB";

    private static final ComponentType DEFAULT_COM_TYPE = ComponentType.MODULE;
    private static final ComponentType UPDATED_COM_TYPE = ComponentType.MIDDLEWARE;

    @Autowired
    private ComponentEntityRepository componentEntityRepository;

    @Autowired
    private ComponentEntityService componentEntityService;

    @Autowired
    private ComponentEntityQueryService componentEntityQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restComponentEntityMockMvc;

    private ComponentEntity componentEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ComponentEntityResource componentEntityResource = new ComponentEntityResource(componentEntityService, componentEntityQueryService);
        this.restComponentEntityMockMvc = MockMvcBuilders.standaloneSetup(componentEntityResource)
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
    public static ComponentEntity createEntity(EntityManager em) {
        ComponentEntity componentEntity = new ComponentEntity()
            .comName(DEFAULT_COM_NAME)
            .comType(DEFAULT_COM_TYPE);
        // Add required entity
        Application application = ApplicationResourceIntTest.createEntity(em);
        em.persist(application);
        em.flush();
        componentEntity.setApp(application);
        // Add required entity
        Server server = ServerResourceIntTest.createEntity(em);
        em.persist(server);
        em.flush();
        componentEntity.setServer(server);
        return componentEntity;
    }

    @Before
    public void initTest() {
        componentEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createComponentEntity() throws Exception {
        int databaseSizeBeforeCreate = componentEntityRepository.findAll().size();

        // Create the ComponentEntity
        restComponentEntityMockMvc.perform(post("/api/component-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(componentEntity)))
            .andExpect(status().isCreated());

        // Validate the ComponentEntity in the database
        List<ComponentEntity> componentEntityList = componentEntityRepository.findAll();
        assertThat(componentEntityList).hasSize(databaseSizeBeforeCreate + 1);
        ComponentEntity testComponentEntity = componentEntityList.get(componentEntityList.size() - 1);
        assertThat(testComponentEntity.getComName()).isEqualTo(DEFAULT_COM_NAME);
        assertThat(testComponentEntity.getComType()).isEqualTo(DEFAULT_COM_TYPE);
    }

    @Test
    @Transactional
    public void createComponentEntityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = componentEntityRepository.findAll().size();

        // Create the ComponentEntity with an existing ID
        componentEntity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComponentEntityMockMvc.perform(post("/api/component-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(componentEntity)))
            .andExpect(status().isBadRequest());

        // Validate the ComponentEntity in the database
        List<ComponentEntity> componentEntityList = componentEntityRepository.findAll();
        assertThat(componentEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkComNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = componentEntityRepository.findAll().size();
        // set the field null
        componentEntity.setComName(null);

        // Create the ComponentEntity, which fails.

        restComponentEntityMockMvc.perform(post("/api/component-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(componentEntity)))
            .andExpect(status().isBadRequest());

        List<ComponentEntity> componentEntityList = componentEntityRepository.findAll();
        assertThat(componentEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkComTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = componentEntityRepository.findAll().size();
        // set the field null
        componentEntity.setComType(null);

        // Create the ComponentEntity, which fails.

        restComponentEntityMockMvc.perform(post("/api/component-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(componentEntity)))
            .andExpect(status().isBadRequest());

        List<ComponentEntity> componentEntityList = componentEntityRepository.findAll();
        assertThat(componentEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComponentEntities() throws Exception {
        // Initialize the database
        componentEntityRepository.saveAndFlush(componentEntity);

        // Get all the componentEntityList
        restComponentEntityMockMvc.perform(get("/api/component-entities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(componentEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].comName").value(hasItem(DEFAULT_COM_NAME.toString())))
            .andExpect(jsonPath("$.[*].comType").value(hasItem(DEFAULT_COM_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getComponentEntity() throws Exception {
        // Initialize the database
        componentEntityRepository.saveAndFlush(componentEntity);

        // Get the componentEntity
        restComponentEntityMockMvc.perform(get("/api/component-entities/{id}", componentEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(componentEntity.getId().intValue()))
            .andExpect(jsonPath("$.comName").value(DEFAULT_COM_NAME.toString()))
            .andExpect(jsonPath("$.comType").value(DEFAULT_COM_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllComponentEntitiesByComNameIsEqualToSomething() throws Exception {
        // Initialize the database
        componentEntityRepository.saveAndFlush(componentEntity);

        // Get all the componentEntityList where comName equals to DEFAULT_COM_NAME
        defaultComponentEntityShouldBeFound("comName.equals=" + DEFAULT_COM_NAME);

        // Get all the componentEntityList where comName equals to UPDATED_COM_NAME
        defaultComponentEntityShouldNotBeFound("comName.equals=" + UPDATED_COM_NAME);
    }

    @Test
    @Transactional
    public void getAllComponentEntitiesByComNameIsInShouldWork() throws Exception {
        // Initialize the database
        componentEntityRepository.saveAndFlush(componentEntity);

        // Get all the componentEntityList where comName in DEFAULT_COM_NAME or UPDATED_COM_NAME
        defaultComponentEntityShouldBeFound("comName.in=" + DEFAULT_COM_NAME + "," + UPDATED_COM_NAME);

        // Get all the componentEntityList where comName equals to UPDATED_COM_NAME
        defaultComponentEntityShouldNotBeFound("comName.in=" + UPDATED_COM_NAME);
    }

    @Test
    @Transactional
    public void getAllComponentEntitiesByComNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        componentEntityRepository.saveAndFlush(componentEntity);

        // Get all the componentEntityList where comName is not null
        defaultComponentEntityShouldBeFound("comName.specified=true");

        // Get all the componentEntityList where comName is null
        defaultComponentEntityShouldNotBeFound("comName.specified=false");
    }

    @Test
    @Transactional
    public void getAllComponentEntitiesByComTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        componentEntityRepository.saveAndFlush(componentEntity);

        // Get all the componentEntityList where comType equals to DEFAULT_COM_TYPE
        defaultComponentEntityShouldBeFound("comType.equals=" + DEFAULT_COM_TYPE);

        // Get all the componentEntityList where comType equals to UPDATED_COM_TYPE
        defaultComponentEntityShouldNotBeFound("comType.equals=" + UPDATED_COM_TYPE);
    }

    @Test
    @Transactional
    public void getAllComponentEntitiesByComTypeIsInShouldWork() throws Exception {
        // Initialize the database
        componentEntityRepository.saveAndFlush(componentEntity);

        // Get all the componentEntityList where comType in DEFAULT_COM_TYPE or UPDATED_COM_TYPE
        defaultComponentEntityShouldBeFound("comType.in=" + DEFAULT_COM_TYPE + "," + UPDATED_COM_TYPE);

        // Get all the componentEntityList where comType equals to UPDATED_COM_TYPE
        defaultComponentEntityShouldNotBeFound("comType.in=" + UPDATED_COM_TYPE);
    }

    @Test
    @Transactional
    public void getAllComponentEntitiesByComTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        componentEntityRepository.saveAndFlush(componentEntity);

        // Get all the componentEntityList where comType is not null
        defaultComponentEntityShouldBeFound("comType.specified=true");

        // Get all the componentEntityList where comType is null
        defaultComponentEntityShouldNotBeFound("comType.specified=false");
    }

    @Test
    @Transactional
    public void getAllComponentEntitiesByAppIsEqualToSomething() throws Exception {
        // Initialize the database
        Application app = ApplicationResourceIntTest.createEntity(em);
        em.persist(app);
        em.flush();
        componentEntity.setApp(app);
        componentEntityRepository.saveAndFlush(componentEntity);
        Long appId = app.getId();

        // Get all the componentEntityList where app equals to appId
        defaultComponentEntityShouldBeFound("appId.equals=" + appId);

        // Get all the componentEntityList where app equals to appId + 1
        defaultComponentEntityShouldNotBeFound("appId.equals=" + (appId + 1));
    }


    @Test
    @Transactional
    public void getAllComponentEntitiesByServerIsEqualToSomething() throws Exception {
        // Initialize the database
        Server server = ServerResourceIntTest.createEntity(em);
        em.persist(server);
        em.flush();
        componentEntity.setServer(server);
        componentEntityRepository.saveAndFlush(componentEntity);
        Long serverId = server.getId();

        // Get all the componentEntityList where server equals to serverId
        defaultComponentEntityShouldBeFound("serverId.equals=" + serverId);

        // Get all the componentEntityList where server equals to serverId + 1
        defaultComponentEntityShouldNotBeFound("serverId.equals=" + (serverId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultComponentEntityShouldBeFound(String filter) throws Exception {
        restComponentEntityMockMvc.perform(get("/api/component-entities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(componentEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].comName").value(hasItem(DEFAULT_COM_NAME.toString())))
            .andExpect(jsonPath("$.[*].comType").value(hasItem(DEFAULT_COM_TYPE.toString())));

        // Check, that the count call also returns 1
        restComponentEntityMockMvc.perform(get("/api/component-entities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultComponentEntityShouldNotBeFound(String filter) throws Exception {
        restComponentEntityMockMvc.perform(get("/api/component-entities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComponentEntityMockMvc.perform(get("/api/component-entities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingComponentEntity() throws Exception {
        // Get the componentEntity
        restComponentEntityMockMvc.perform(get("/api/component-entities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComponentEntity() throws Exception {
        // Initialize the database
        componentEntityService.save(componentEntity);

        int databaseSizeBeforeUpdate = componentEntityRepository.findAll().size();

        // Update the componentEntity
        ComponentEntity updatedComponentEntity = componentEntityRepository.findById(componentEntity.getId()).get();
        // Disconnect from session so that the updates on updatedComponentEntity are not directly saved in db
        em.detach(updatedComponentEntity);
        updatedComponentEntity
            .comName(UPDATED_COM_NAME)
            .comType(UPDATED_COM_TYPE);

        restComponentEntityMockMvc.perform(put("/api/component-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedComponentEntity)))
            .andExpect(status().isOk());

        // Validate the ComponentEntity in the database
        List<ComponentEntity> componentEntityList = componentEntityRepository.findAll();
        assertThat(componentEntityList).hasSize(databaseSizeBeforeUpdate);
        ComponentEntity testComponentEntity = componentEntityList.get(componentEntityList.size() - 1);
        assertThat(testComponentEntity.getComName()).isEqualTo(UPDATED_COM_NAME);
        assertThat(testComponentEntity.getComType()).isEqualTo(UPDATED_COM_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingComponentEntity() throws Exception {
        int databaseSizeBeforeUpdate = componentEntityRepository.findAll().size();

        // Create the ComponentEntity

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponentEntityMockMvc.perform(put("/api/component-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(componentEntity)))
            .andExpect(status().isBadRequest());

        // Validate the ComponentEntity in the database
        List<ComponentEntity> componentEntityList = componentEntityRepository.findAll();
        assertThat(componentEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteComponentEntity() throws Exception {
        // Initialize the database
        componentEntityService.save(componentEntity);

        int databaseSizeBeforeDelete = componentEntityRepository.findAll().size();

        // Get the componentEntity
        restComponentEntityMockMvc.perform(delete("/api/component-entities/{id}", componentEntity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ComponentEntity> componentEntityList = componentEntityRepository.findAll();
        assertThat(componentEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComponentEntity.class);
        ComponentEntity componentEntity1 = new ComponentEntity();
        componentEntity1.setId(1L);
        ComponentEntity componentEntity2 = new ComponentEntity();
        componentEntity2.setId(componentEntity1.getId());
        assertThat(componentEntity1).isEqualTo(componentEntity2);
        componentEntity2.setId(2L);
        assertThat(componentEntity1).isNotEqualTo(componentEntity2);
        componentEntity1.setId(null);
        assertThat(componentEntity1).isNotEqualTo(componentEntity2);
    }
}
