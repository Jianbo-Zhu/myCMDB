package net.jianbo.cmdb.web.rest;

import net.jianbo.cmdb.MyCmdbApp;

import net.jianbo.cmdb.domain.ComponentEntity;
import net.jianbo.cmdb.domain.Server;
import net.jianbo.cmdb.repository.ComponentEntityRepository;
import net.jianbo.cmdb.service.ComponentEntityService;
import net.jianbo.cmdb.web.rest.errors.ExceptionTranslator;

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
        final ComponentEntityResource componentEntityResource = new ComponentEntityResource(componentEntityService);
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
