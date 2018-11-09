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
import net.jianbo.cmdb.service.dto.ServerCriteria;
import net.jianbo.cmdb.service.ServerQueryService;

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
    private ServerQueryService serverQueryService;

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
        final ServerResource serverResource = new ServerResource(serverService, serverQueryService);
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
    public void getAllServersByHostnameIsEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where hostname equals to DEFAULT_HOSTNAME
        defaultServerShouldBeFound("hostname.equals=" + DEFAULT_HOSTNAME);

        // Get all the serverList where hostname equals to UPDATED_HOSTNAME
        defaultServerShouldNotBeFound("hostname.equals=" + UPDATED_HOSTNAME);
    }

    @Test
    @Transactional
    public void getAllServersByHostnameIsInShouldWork() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where hostname in DEFAULT_HOSTNAME or UPDATED_HOSTNAME
        defaultServerShouldBeFound("hostname.in=" + DEFAULT_HOSTNAME + "," + UPDATED_HOSTNAME);

        // Get all the serverList where hostname equals to UPDATED_HOSTNAME
        defaultServerShouldNotBeFound("hostname.in=" + UPDATED_HOSTNAME);
    }

    @Test
    @Transactional
    public void getAllServersByHostnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where hostname is not null
        defaultServerShouldBeFound("hostname.specified=true");

        // Get all the serverList where hostname is null
        defaultServerShouldNotBeFound("hostname.specified=false");
    }

    @Test
    @Transactional
    public void getAllServersByIpAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where ipAddress equals to DEFAULT_IP_ADDRESS
        defaultServerShouldBeFound("ipAddress.equals=" + DEFAULT_IP_ADDRESS);

        // Get all the serverList where ipAddress equals to UPDATED_IP_ADDRESS
        defaultServerShouldNotBeFound("ipAddress.equals=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllServersByIpAddressIsInShouldWork() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where ipAddress in DEFAULT_IP_ADDRESS or UPDATED_IP_ADDRESS
        defaultServerShouldBeFound("ipAddress.in=" + DEFAULT_IP_ADDRESS + "," + UPDATED_IP_ADDRESS);

        // Get all the serverList where ipAddress equals to UPDATED_IP_ADDRESS
        defaultServerShouldNotBeFound("ipAddress.in=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllServersByIpAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where ipAddress is not null
        defaultServerShouldBeFound("ipAddress.specified=true");

        // Get all the serverList where ipAddress is null
        defaultServerShouldNotBeFound("ipAddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllServersByMacAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where macAddress equals to DEFAULT_MAC_ADDRESS
        defaultServerShouldBeFound("macAddress.equals=" + DEFAULT_MAC_ADDRESS);

        // Get all the serverList where macAddress equals to UPDATED_MAC_ADDRESS
        defaultServerShouldNotBeFound("macAddress.equals=" + UPDATED_MAC_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllServersByMacAddressIsInShouldWork() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where macAddress in DEFAULT_MAC_ADDRESS or UPDATED_MAC_ADDRESS
        defaultServerShouldBeFound("macAddress.in=" + DEFAULT_MAC_ADDRESS + "," + UPDATED_MAC_ADDRESS);

        // Get all the serverList where macAddress equals to UPDATED_MAC_ADDRESS
        defaultServerShouldNotBeFound("macAddress.in=" + UPDATED_MAC_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllServersByMacAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where macAddress is not null
        defaultServerShouldBeFound("macAddress.specified=true");

        // Get all the serverList where macAddress is null
        defaultServerShouldNotBeFound("macAddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllServersByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where position equals to DEFAULT_POSITION
        defaultServerShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the serverList where position equals to UPDATED_POSITION
        defaultServerShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllServersByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultServerShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the serverList where position equals to UPDATED_POSITION
        defaultServerShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllServersByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where position is not null
        defaultServerShouldBeFound("position.specified=true");

        // Get all the serverList where position is null
        defaultServerShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    public void getAllServersByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where brand equals to DEFAULT_BRAND
        defaultServerShouldBeFound("brand.equals=" + DEFAULT_BRAND);

        // Get all the serverList where brand equals to UPDATED_BRAND
        defaultServerShouldNotBeFound("brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    public void getAllServersByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where brand in DEFAULT_BRAND or UPDATED_BRAND
        defaultServerShouldBeFound("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND);

        // Get all the serverList where brand equals to UPDATED_BRAND
        defaultServerShouldNotBeFound("brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    public void getAllServersByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where brand is not null
        defaultServerShouldBeFound("brand.specified=true");

        // Get all the serverList where brand is null
        defaultServerShouldNotBeFound("brand.specified=false");
    }

    @Test
    @Transactional
    public void getAllServersByMemSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where memSize equals to DEFAULT_MEM_SIZE
        defaultServerShouldBeFound("memSize.equals=" + DEFAULT_MEM_SIZE);

        // Get all the serverList where memSize equals to UPDATED_MEM_SIZE
        defaultServerShouldNotBeFound("memSize.equals=" + UPDATED_MEM_SIZE);
    }

    @Test
    @Transactional
    public void getAllServersByMemSizeIsInShouldWork() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where memSize in DEFAULT_MEM_SIZE or UPDATED_MEM_SIZE
        defaultServerShouldBeFound("memSize.in=" + DEFAULT_MEM_SIZE + "," + UPDATED_MEM_SIZE);

        // Get all the serverList where memSize equals to UPDATED_MEM_SIZE
        defaultServerShouldNotBeFound("memSize.in=" + UPDATED_MEM_SIZE);
    }

    @Test
    @Transactional
    public void getAllServersByMemSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where memSize is not null
        defaultServerShouldBeFound("memSize.specified=true");

        // Get all the serverList where memSize is null
        defaultServerShouldNotBeFound("memSize.specified=false");
    }

    @Test
    @Transactional
    public void getAllServersByMemSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where memSize greater than or equals to DEFAULT_MEM_SIZE
        defaultServerShouldBeFound("memSize.greaterOrEqualThan=" + DEFAULT_MEM_SIZE);

        // Get all the serverList where memSize greater than or equals to UPDATED_MEM_SIZE
        defaultServerShouldNotBeFound("memSize.greaterOrEqualThan=" + UPDATED_MEM_SIZE);
    }

    @Test
    @Transactional
    public void getAllServersByMemSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where memSize less than or equals to DEFAULT_MEM_SIZE
        defaultServerShouldNotBeFound("memSize.lessThan=" + DEFAULT_MEM_SIZE);

        // Get all the serverList where memSize less than or equals to UPDATED_MEM_SIZE
        defaultServerShouldBeFound("memSize.lessThan=" + UPDATED_MEM_SIZE);
    }


    @Test
    @Transactional
    public void getAllServersByCoreNoIsEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where coreNo equals to DEFAULT_CORE_NO
        defaultServerShouldBeFound("coreNo.equals=" + DEFAULT_CORE_NO);

        // Get all the serverList where coreNo equals to UPDATED_CORE_NO
        defaultServerShouldNotBeFound("coreNo.equals=" + UPDATED_CORE_NO);
    }

    @Test
    @Transactional
    public void getAllServersByCoreNoIsInShouldWork() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where coreNo in DEFAULT_CORE_NO or UPDATED_CORE_NO
        defaultServerShouldBeFound("coreNo.in=" + DEFAULT_CORE_NO + "," + UPDATED_CORE_NO);

        // Get all the serverList where coreNo equals to UPDATED_CORE_NO
        defaultServerShouldNotBeFound("coreNo.in=" + UPDATED_CORE_NO);
    }

    @Test
    @Transactional
    public void getAllServersByCoreNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where coreNo is not null
        defaultServerShouldBeFound("coreNo.specified=true");

        // Get all the serverList where coreNo is null
        defaultServerShouldNotBeFound("coreNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllServersByCoreNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where coreNo greater than or equals to DEFAULT_CORE_NO
        defaultServerShouldBeFound("coreNo.greaterOrEqualThan=" + DEFAULT_CORE_NO);

        // Get all the serverList where coreNo greater than or equals to UPDATED_CORE_NO
        defaultServerShouldNotBeFound("coreNo.greaterOrEqualThan=" + UPDATED_CORE_NO);
    }

    @Test
    @Transactional
    public void getAllServersByCoreNoIsLessThanSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where coreNo less than or equals to DEFAULT_CORE_NO
        defaultServerShouldNotBeFound("coreNo.lessThan=" + DEFAULT_CORE_NO);

        // Get all the serverList where coreNo less than or equals to UPDATED_CORE_NO
        defaultServerShouldBeFound("coreNo.lessThan=" + UPDATED_CORE_NO);
    }


    @Test
    @Transactional
    public void getAllServersByOsVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where osVersion equals to DEFAULT_OS_VERSION
        defaultServerShouldBeFound("osVersion.equals=" + DEFAULT_OS_VERSION);

        // Get all the serverList where osVersion equals to UPDATED_OS_VERSION
        defaultServerShouldNotBeFound("osVersion.equals=" + UPDATED_OS_VERSION);
    }

    @Test
    @Transactional
    public void getAllServersByOsVersionIsInShouldWork() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where osVersion in DEFAULT_OS_VERSION or UPDATED_OS_VERSION
        defaultServerShouldBeFound("osVersion.in=" + DEFAULT_OS_VERSION + "," + UPDATED_OS_VERSION);

        // Get all the serverList where osVersion equals to UPDATED_OS_VERSION
        defaultServerShouldNotBeFound("osVersion.in=" + UPDATED_OS_VERSION);
    }

    @Test
    @Transactional
    public void getAllServersByOsVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where osVersion is not null
        defaultServerShouldBeFound("osVersion.specified=true");

        // Get all the serverList where osVersion is null
        defaultServerShouldNotBeFound("osVersion.specified=false");
    }

    @Test
    @Transactional
    public void getAllServersByVendorIsEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where vendor equals to DEFAULT_VENDOR
        defaultServerShouldBeFound("vendor.equals=" + DEFAULT_VENDOR);

        // Get all the serverList where vendor equals to UPDATED_VENDOR
        defaultServerShouldNotBeFound("vendor.equals=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    public void getAllServersByVendorIsInShouldWork() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where vendor in DEFAULT_VENDOR or UPDATED_VENDOR
        defaultServerShouldBeFound("vendor.in=" + DEFAULT_VENDOR + "," + UPDATED_VENDOR);

        // Get all the serverList where vendor equals to UPDATED_VENDOR
        defaultServerShouldNotBeFound("vendor.in=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    public void getAllServersByVendorIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where vendor is not null
        defaultServerShouldBeFound("vendor.specified=true");

        // Get all the serverList where vendor is null
        defaultServerShouldNotBeFound("vendor.specified=false");
    }

    @Test
    @Transactional
    public void getAllServersByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where purchaseDate equals to DEFAULT_PURCHASE_DATE
        defaultServerShouldBeFound("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE);

        // Get all the serverList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultServerShouldNotBeFound("purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllServersByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where purchaseDate in DEFAULT_PURCHASE_DATE or UPDATED_PURCHASE_DATE
        defaultServerShouldBeFound("purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE);

        // Get all the serverList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultServerShouldNotBeFound("purchaseDate.in=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllServersByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where purchaseDate is not null
        defaultServerShouldBeFound("purchaseDate.specified=true");

        // Get all the serverList where purchaseDate is null
        defaultServerShouldNotBeFound("purchaseDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllServersByPurchaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where purchaseDate greater than or equals to DEFAULT_PURCHASE_DATE
        defaultServerShouldBeFound("purchaseDate.greaterOrEqualThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the serverList where purchaseDate greater than or equals to UPDATED_PURCHASE_DATE
        defaultServerShouldNotBeFound("purchaseDate.greaterOrEqualThan=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllServersByPurchaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where purchaseDate less than or equals to DEFAULT_PURCHASE_DATE
        defaultServerShouldNotBeFound("purchaseDate.lessThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the serverList where purchaseDate less than or equals to UPDATED_PURCHASE_DATE
        defaultServerShouldBeFound("purchaseDate.lessThan=" + UPDATED_PURCHASE_DATE);
    }


    @Test
    @Transactional
    public void getAllServersByWarrantyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where warrantyDate equals to DEFAULT_WARRANTY_DATE
        defaultServerShouldBeFound("warrantyDate.equals=" + DEFAULT_WARRANTY_DATE);

        // Get all the serverList where warrantyDate equals to UPDATED_WARRANTY_DATE
        defaultServerShouldNotBeFound("warrantyDate.equals=" + UPDATED_WARRANTY_DATE);
    }

    @Test
    @Transactional
    public void getAllServersByWarrantyDateIsInShouldWork() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where warrantyDate in DEFAULT_WARRANTY_DATE or UPDATED_WARRANTY_DATE
        defaultServerShouldBeFound("warrantyDate.in=" + DEFAULT_WARRANTY_DATE + "," + UPDATED_WARRANTY_DATE);

        // Get all the serverList where warrantyDate equals to UPDATED_WARRANTY_DATE
        defaultServerShouldNotBeFound("warrantyDate.in=" + UPDATED_WARRANTY_DATE);
    }

    @Test
    @Transactional
    public void getAllServersByWarrantyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where warrantyDate is not null
        defaultServerShouldBeFound("warrantyDate.specified=true");

        // Get all the serverList where warrantyDate is null
        defaultServerShouldNotBeFound("warrantyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllServersByWarrantyDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where warrantyDate greater than or equals to DEFAULT_WARRANTY_DATE
        defaultServerShouldBeFound("warrantyDate.greaterOrEqualThan=" + DEFAULT_WARRANTY_DATE);

        // Get all the serverList where warrantyDate greater than or equals to UPDATED_WARRANTY_DATE
        defaultServerShouldNotBeFound("warrantyDate.greaterOrEqualThan=" + UPDATED_WARRANTY_DATE);
    }

    @Test
    @Transactional
    public void getAllServersByWarrantyDateIsLessThanSomething() throws Exception {
        // Initialize the database
        serverRepository.saveAndFlush(server);

        // Get all the serverList where warrantyDate less than or equals to DEFAULT_WARRANTY_DATE
        defaultServerShouldNotBeFound("warrantyDate.lessThan=" + DEFAULT_WARRANTY_DATE);

        // Get all the serverList where warrantyDate less than or equals to UPDATED_WARRANTY_DATE
        defaultServerShouldBeFound("warrantyDate.lessThan=" + UPDATED_WARRANTY_DATE);
    }


    @Test
    @Transactional
    public void getAllServersByComponentsIsEqualToSomething() throws Exception {
        // Initialize the database
        ComponentEntity components = ComponentEntityResourceIntTest.createEntity(em);
        em.persist(components);
        em.flush();
        server.addComponents(components);
        serverRepository.saveAndFlush(server);
        Long componentsId = components.getId();

        // Get all the serverList where components equals to componentsId
        defaultServerShouldBeFound("componentsId.equals=" + componentsId);

        // Get all the serverList where components equals to componentsId + 1
        defaultServerShouldNotBeFound("componentsId.equals=" + (componentsId + 1));
    }


    @Test
    @Transactional
    public void getAllServersByDataCenterIsEqualToSomething() throws Exception {
        // Initialize the database
        DataCenter dataCenter = DataCenterResourceIntTest.createEntity(em);
        em.persist(dataCenter);
        em.flush();
        server.setDataCenter(dataCenter);
        serverRepository.saveAndFlush(server);
        Long dataCenterId = dataCenter.getId();

        // Get all the serverList where dataCenter equals to dataCenterId
        defaultServerShouldBeFound("dataCenterId.equals=" + dataCenterId);

        // Get all the serverList where dataCenter equals to dataCenterId + 1
        defaultServerShouldNotBeFound("dataCenterId.equals=" + (dataCenterId + 1));
    }


    @Test
    @Transactional
    public void getAllServersByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        Contactor owner = ContactorResourceIntTest.createEntity(em);
        em.persist(owner);
        em.flush();
        server.setOwner(owner);
        serverRepository.saveAndFlush(server);
        Long ownerId = owner.getId();

        // Get all the serverList where owner equals to ownerId
        defaultServerShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the serverList where owner equals to ownerId + 1
        defaultServerShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }


    @Test
    @Transactional
    public void getAllServersByVendorContactIsEqualToSomething() throws Exception {
        // Initialize the database
        Contactor vendorContact = ContactorResourceIntTest.createEntity(em);
        em.persist(vendorContact);
        em.flush();
        server.setVendorContact(vendorContact);
        serverRepository.saveAndFlush(server);
        Long vendorContactId = vendorContact.getId();

        // Get all the serverList where vendorContact equals to vendorContactId
        defaultServerShouldBeFound("vendorContactId.equals=" + vendorContactId);

        // Get all the serverList where vendorContact equals to vendorContactId + 1
        defaultServerShouldNotBeFound("vendorContactId.equals=" + (vendorContactId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultServerShouldBeFound(String filter) throws Exception {
        restServerMockMvc.perform(get("/api/servers?sort=id,desc&" + filter))
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

        // Check, that the count call also returns 1
        restServerMockMvc.perform(get("/api/servers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultServerShouldNotBeFound(String filter) throws Exception {
        restServerMockMvc.perform(get("/api/servers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServerMockMvc.perform(get("/api/servers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
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
