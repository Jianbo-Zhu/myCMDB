package net.jianbo.cmdb.web.rest;

import net.jianbo.cmdb.MyCmdbApp;

import net.jianbo.cmdb.domain.Server;
import net.jianbo.cmdb.domain.ComponentEntity;
import net.jianbo.cmdb.domain.DataCenter;
import net.jianbo.cmdb.domain.Contactor;
import net.jianbo.cmdb.domain.Contactor;
import net.jianbo.cmdb.repository.ServerRepository;
import net.jianbo.cmdb.service.ServerService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static net.jianbo.cmdb.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ServerResource REST controller.
 *
 * @see ServerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyCmdbApp.class)
public class ServerResourceIntTest {

    private static final String DEFAULT_HOSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_HOSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_MAC_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_MAC_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final Integer DEFAULT_MEM_SIZE = 1;
    private static final Integer UPDATED_MEM_SIZE = 2;

    private static final Integer DEFAULT_CORE_NO = 1;
    private static final Integer UPDATED_CORE_NO = 2;

    private static final String DEFAULT_OS_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_OS_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_WARRANTY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_WARRANTY_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ServerService serverService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServerMockMvc;

    private Server server;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServerResource serverResource = new ServerResource(serverService);
        this.restServerMockMvc = MockMvcBuilders.standaloneSetup(serverResource)
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
    public static Server createEntity(EntityManager em) {
        Server server = new Server()
            .hostname(DEFAULT_HOSTNAME)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .macAddress(DEFAULT_MAC_ADDRESS)
            .position(DEFAULT_POSITION)
            .brand(DEFAULT_BRAND)
            .memSize(DEFAULT_MEM_SIZE)
            .coreNo(DEFAULT_CORE_NO)
            .osVersion(DEFAULT_OS_VERSION)
            .vendor(DEFAULT_VENDOR)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .warrantyDate(DEFAULT_WARRANTY_DATE);
        // Add required entity
        ComponentEntity componentEntity = ComponentEntityResourceIntTest.createEntity(em);
        em.persist(componentEntity);
        em.flush();
        server.getComponents().add(componentEntity);
        // Add required entity
        DataCenter dataCenter = DataCenterResourceIntTest.createEntity(em);
        em.persist(dataCenter);
        em.flush();
        server.setDataCenter(dataCenter);
        // Add required entity
        Contactor contactor = ContactorResourceIntTest.createEntity(em);
        em.persist(contactor);
        em.flush();
        server.setOwner(contactor);
        // Add required entity
        server.setVendorContact(contactor);
        return server;
    }

    @Before
    public void initTest() {
        server = createEntity(em);
    }

    @Test
    @Transactional
    public void createServer() throws Exception {
        int databaseSizeBeforeCreate = serverRepository.findAll().size();

        // Create the Server
        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isCreated());

        // Validate the Server in the database
        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeCreate + 1);
        Server testServer = serverList.get(serverList.size() - 1);
        assertThat(testServer.getHostname()).isEqualTo(DEFAULT_HOSTNAME);
        assertThat(testServer.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testServer.getMacAddress()).isEqualTo(DEFAULT_MAC_ADDRESS);
        assertThat(testServer.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testServer.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testServer.getMemSize()).isEqualTo(DEFAULT_MEM_SIZE);
        assertThat(testServer.getCoreNo()).isEqualTo(DEFAULT_CORE_NO);
        assertThat(testServer.getOsVersion()).isEqualTo(DEFAULT_OS_VERSION);
        assertThat(testServer.getVendor()).isEqualTo(DEFAULT_VENDOR);
        assertThat(testServer.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testServer.getWarrantyDate()).isEqualTo(DEFAULT_WARRANTY_DATE);
    }

    @Test
    @Transactional
    public void createServerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serverRepository.findAll().size();

        // Create the Server with an existing ID
        server.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        // Validate the Server in the database
        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHostnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().size();
        // set the field null
        server.setHostname(null);

        // Create the Server, which fails.

        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIpAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().size();
        // set the field null
        server.setIpAddress(null);

        // Create the Server, which fails.

        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMacAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().size();
        // set the field null
        server.setMacAddress(null);

        // Create the Server, which fails.

        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().size();
        // set the field null
        server.setPosition(null);

        // Create the Server, which fails.

        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBrandIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().size();
        // set the field null
        server.setBrand(null);

        // Create the Server, which fails.

        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMemSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().size();
        // set the field null
        server.setMemSize(null);

        // Create the Server, which fails.

        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCoreNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().size();
        // set the field null
        server.setCoreNo(null);

        // Create the Server, which fails.

        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOsVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().size();
        // set the field null
        server.setOsVersion(null);

        // Create the Server, which fails.

        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVendorIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().size();
        // set the field null
        server.setVendor(null);

        // Create the Server, which fails.

        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPurchaseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().size();
        // set the field null
        server.setPurchaseDate(null);

        // Create the Server, which fails.

        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWarrantyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().size();
        // set the field null
        server.setWarrantyDate(null);

        // Create the Server, which fails.

        restServerMockMvc.perform(post("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServers() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList
        restServerMockMvc.perform(get("/api/servers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(server.getId().intValue())))
            .andExpect(jsonPath("$.[*].hostname").value(hasItem(DEFAULT_HOSTNAME.toString())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].macAddress").value(hasItem(DEFAULT_MAC_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
            .andExpect(jsonPath("$.[*].memSize").value(hasItem(DEFAULT_MEM_SIZE)))
            .andExpect(jsonPath("$.[*].coreNo").value(hasItem(DEFAULT_CORE_NO)))
            .andExpect(jsonPath("$.[*].osVersion").value(hasItem(DEFAULT_OS_VERSION.toString())))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR.toString())))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].warrantyDate").value(hasItem(DEFAULT_WARRANTY_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getServer() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get the server
        restServerMockMvc.perform(get("/api/servers/{id}", server.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(server.getId().intValue()))
            .andExpect(jsonPath("$.hostname").value(DEFAULT_HOSTNAME.toString()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS.toString()))
            .andExpect(jsonPath("$.macAddress").value(DEFAULT_MAC_ADDRESS.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()))
            .andExpect(jsonPath("$.memSize").value(DEFAULT_MEM_SIZE))
            .andExpect(jsonPath("$.coreNo").value(DEFAULT_CORE_NO))
            .andExpect(jsonPath("$.osVersion").value(DEFAULT_OS_VERSION.toString()))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR.toString()))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.warrantyDate").value(DEFAULT_WARRANTY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServer() throws Exception {
        // Get the server
        restServerMockMvc.perform(get("/api/servers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServer() throws Exception {
        // Initialize the database
        serverService.save(server);

        int databaseSizeBeforeUpdate = serverRepository.findAll().size();

        // Update the server
        Server updatedServer = serverRepository.findById(server.getId()).get();
        // Disconnect from session so that the updates on updatedServer are not directly saved in db
        em.detach(updatedServer);
        updatedServer
            .hostname(UPDATED_HOSTNAME)
            .ipAddress(UPDATED_IP_ADDRESS)
            .macAddress(UPDATED_MAC_ADDRESS)
            .position(UPDATED_POSITION)
            .brand(UPDATED_BRAND)
            .memSize(UPDATED_MEM_SIZE)
            .coreNo(UPDATED_CORE_NO)
            .osVersion(UPDATED_OS_VERSION)
            .vendor(UPDATED_VENDOR)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .warrantyDate(UPDATED_WARRANTY_DATE);

        restServerMockMvc.perform(put("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServer)))
            .andExpect(status().isOk());

        // Validate the Server in the database
        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeUpdate);
        Server testServer = serverList.get(serverList.size() - 1);
        assertThat(testServer.getHostname()).isEqualTo(UPDATED_HOSTNAME);
        assertThat(testServer.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testServer.getMacAddress()).isEqualTo(UPDATED_MAC_ADDRESS);
        assertThat(testServer.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testServer.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testServer.getMemSize()).isEqualTo(UPDATED_MEM_SIZE);
        assertThat(testServer.getCoreNo()).isEqualTo(UPDATED_CORE_NO);
        assertThat(testServer.getOsVersion()).isEqualTo(UPDATED_OS_VERSION);
        assertThat(testServer.getVendor()).isEqualTo(UPDATED_VENDOR);
        assertThat(testServer.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testServer.getWarrantyDate()).isEqualTo(UPDATED_WARRANTY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingServer() throws Exception {
        int databaseSizeBeforeUpdate = serverRepository.findAll().size();

        // Create the Server

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServerMockMvc.perform(put("/api/servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(server)))
            .andExpect(status().isBadRequest());

        // Validate the Server in the database
        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServer() throws Exception {
        // Initialize the database
        serverService.save(server);

        int databaseSizeBeforeDelete = serverRepository.findAll().size();

        // Get the server
        restServerMockMvc.perform(delete("/api/servers/{id}", server.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Server> serverList = serverRepository.findAll();
        assertThat(serverList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Server.class);
        Server server1 = new Server();
        server1.setId(1L);
        Server server2 = new Server();
        server2.setId(server1.getId());
        assertThat(server1).isEqualTo(server2);
        server2.setId(2L);
        assertThat(server1).isNotEqualTo(server2);
        server1.setId(null);
        assertThat(server1).isNotEqualTo(server2);
    }
}
