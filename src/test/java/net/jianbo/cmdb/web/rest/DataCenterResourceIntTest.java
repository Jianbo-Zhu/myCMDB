package net.jianbo.cmdb.web.rest;

import net.jianbo.cmdb.MyCmdbApp;

import net.jianbo.cmdb.domain.DataCenter;
import net.jianbo.cmdb.domain.Contactor;
import net.jianbo.cmdb.repository.DataCenterRepository;
import net.jianbo.cmdb.service.DataCenterService;
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

/**
 * Test class for the DataCenterResource REST controller.
 *
 * @see DataCenterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyCmdbApp.class)
public class DataCenterResourceIntTest {

    private static final String DEFAULT_DC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DC_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private DataCenterRepository dataCenterRepository;

    @Autowired
    private DataCenterService dataCenterService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataCenterMockMvc;

    private DataCenter dataCenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataCenterResource dataCenterResource = new DataCenterResource(dataCenterService);
        this.restDataCenterMockMvc = MockMvcBuilders.standaloneSetup(dataCenterResource)
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
    public static DataCenter createEntity(EntityManager em) {
        DataCenter dataCenter = new DataCenter()
            .dcName(DEFAULT_DC_NAME)
            .address(DEFAULT_ADDRESS);
        // Add required entity
        Contactor contactor = ContactorResourceIntTest.createEntity(em);
        em.persist(contactor);
        em.flush();
        dataCenter.setContactor(contactor);
        return dataCenter;
    }

    @Before
    public void initTest() {
        dataCenter = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataCenter() throws Exception {
        int databaseSizeBeforeCreate = dataCenterRepository.findAll().size();

        // Create the DataCenter
        restDataCenterMockMvc.perform(post("/api/data-centers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataCenter)))
            .andExpect(status().isCreated());

        // Validate the DataCenter in the database
        List<DataCenter> dataCenterList = dataCenterRepository.findAll();
        assertThat(dataCenterList).hasSize(databaseSizeBeforeCreate + 1);
        DataCenter testDataCenter = dataCenterList.get(dataCenterList.size() - 1);
        assertThat(testDataCenter.getDcName()).isEqualTo(DEFAULT_DC_NAME);
        assertThat(testDataCenter.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createDataCenterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataCenterRepository.findAll().size();

        // Create the DataCenter with an existing ID
        dataCenter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataCenterMockMvc.perform(post("/api/data-centers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataCenter)))
            .andExpect(status().isBadRequest());

        // Validate the DataCenter in the database
        List<DataCenter> dataCenterList = dataCenterRepository.findAll();
        assertThat(dataCenterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDcNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataCenterRepository.findAll().size();
        // set the field null
        dataCenter.setDcName(null);

        // Create the DataCenter, which fails.

        restDataCenterMockMvc.perform(post("/api/data-centers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataCenter)))
            .andExpect(status().isBadRequest());

        List<DataCenter> dataCenterList = dataCenterRepository.findAll();
        assertThat(dataCenterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataCenterRepository.findAll().size();
        // set the field null
        dataCenter.setAddress(null);

        // Create the DataCenter, which fails.

        restDataCenterMockMvc.perform(post("/api/data-centers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataCenter)))
            .andExpect(status().isBadRequest());

        List<DataCenter> dataCenterList = dataCenterRepository.findAll();
        assertThat(dataCenterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDataCenters() throws Exception {
        // Initialize the database
        dataCenterRepository.saveAndFlush(dataCenter);

        // Get all the dataCenterList
        restDataCenterMockMvc.perform(get("/api/data-centers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataCenter.getId().intValue())))
            .andExpect(jsonPath("$.[*].dcName").value(hasItem(DEFAULT_DC_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }
    
    @Test
    @Transactional
    public void getDataCenter() throws Exception {
        // Initialize the database
        dataCenterRepository.saveAndFlush(dataCenter);

        // Get the dataCenter
        restDataCenterMockMvc.perform(get("/api/data-centers/{id}", dataCenter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataCenter.getId().intValue()))
            .andExpect(jsonPath("$.dcName").value(DEFAULT_DC_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDataCenter() throws Exception {
        // Get the dataCenter
        restDataCenterMockMvc.perform(get("/api/data-centers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataCenter() throws Exception {
        // Initialize the database
        dataCenterService.save(dataCenter);

        int databaseSizeBeforeUpdate = dataCenterRepository.findAll().size();

        // Update the dataCenter
        DataCenter updatedDataCenter = dataCenterRepository.findById(dataCenter.getId()).get();
        // Disconnect from session so that the updates on updatedDataCenter are not directly saved in db
        em.detach(updatedDataCenter);
        updatedDataCenter
            .dcName(UPDATED_DC_NAME)
            .address(UPDATED_ADDRESS);

        restDataCenterMockMvc.perform(put("/api/data-centers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataCenter)))
            .andExpect(status().isOk());

        // Validate the DataCenter in the database
        List<DataCenter> dataCenterList = dataCenterRepository.findAll();
        assertThat(dataCenterList).hasSize(databaseSizeBeforeUpdate);
        DataCenter testDataCenter = dataCenterList.get(dataCenterList.size() - 1);
        assertThat(testDataCenter.getDcName()).isEqualTo(UPDATED_DC_NAME);
        assertThat(testDataCenter.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingDataCenter() throws Exception {
        int databaseSizeBeforeUpdate = dataCenterRepository.findAll().size();

        // Create the DataCenter

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataCenterMockMvc.perform(put("/api/data-centers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataCenter)))
            .andExpect(status().isBadRequest());

        // Validate the DataCenter in the database
        List<DataCenter> dataCenterList = dataCenterRepository.findAll();
        assertThat(dataCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDataCenter() throws Exception {
        // Initialize the database
        dataCenterService.save(dataCenter);

        int databaseSizeBeforeDelete = dataCenterRepository.findAll().size();

        // Get the dataCenter
        restDataCenterMockMvc.perform(delete("/api/data-centers/{id}", dataCenter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataCenter> dataCenterList = dataCenterRepository.findAll();
        assertThat(dataCenterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataCenter.class);
        DataCenter dataCenter1 = new DataCenter();
        dataCenter1.setId(1L);
        DataCenter dataCenter2 = new DataCenter();
        dataCenter2.setId(dataCenter1.getId());
        assertThat(dataCenter1).isEqualTo(dataCenter2);
        dataCenter2.setId(2L);
        assertThat(dataCenter1).isNotEqualTo(dataCenter2);
        dataCenter1.setId(null);
        assertThat(dataCenter1).isNotEqualTo(dataCenter2);
    }
}
