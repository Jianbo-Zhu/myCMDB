package net.jianbo.cmdb.web.rest;

import net.jianbo.cmdb.MyCmdbApp;

import net.jianbo.cmdb.domain.Contactor;
import net.jianbo.cmdb.repository.ContactorRepository;
import net.jianbo.cmdb.service.ContactorService;
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
 * Test class for the ContactorResource REST controller.
 *
 * @see ContactorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyCmdbApp.class)
public class ContactorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private ContactorRepository contactorRepository;

    @Autowired
    private ContactorService contactorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContactorMockMvc;

    private Contactor contactor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContactorResource contactorResource = new ContactorResource(contactorService);
        this.restContactorMockMvc = MockMvcBuilders.standaloneSetup(contactorResource)
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
    public static Contactor createEntity(EntityManager em) {
        Contactor contactor = new Contactor()
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE);
        return contactor;
    }

    @Before
    public void initTest() {
        contactor = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactor() throws Exception {
        int databaseSizeBeforeCreate = contactorRepository.findAll().size();

        // Create the Contactor
        restContactorMockMvc.perform(post("/api/contactors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactor)))
            .andExpect(status().isCreated());

        // Validate the Contactor in the database
        List<Contactor> contactorList = contactorRepository.findAll();
        assertThat(contactorList).hasSize(databaseSizeBeforeCreate + 1);
        Contactor testContactor = contactorList.get(contactorList.size() - 1);
        assertThat(testContactor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContactor.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createContactorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactorRepository.findAll().size();

        // Create the Contactor with an existing ID
        contactor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactorMockMvc.perform(post("/api/contactors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactor)))
            .andExpect(status().isBadRequest());

        // Validate the Contactor in the database
        List<Contactor> contactorList = contactorRepository.findAll();
        assertThat(contactorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactorRepository.findAll().size();
        // set the field null
        contactor.setName(null);

        // Create the Contactor, which fails.

        restContactorMockMvc.perform(post("/api/contactors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactor)))
            .andExpect(status().isBadRequest());

        List<Contactor> contactorList = contactorRepository.findAll();
        assertThat(contactorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactorRepository.findAll().size();
        // set the field null
        contactor.setPhone(null);

        // Create the Contactor, which fails.

        restContactorMockMvc.perform(post("/api/contactors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactor)))
            .andExpect(status().isBadRequest());

        List<Contactor> contactorList = contactorRepository.findAll();
        assertThat(contactorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactors() throws Exception {
        // Initialize the database
        contactorRepository.saveAndFlush(contactor);

        // Get all the contactorList
        restContactorMockMvc.perform(get("/api/contactors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }
    
    @Test
    @Transactional
    public void getContactor() throws Exception {
        // Initialize the database
        contactorRepository.saveAndFlush(contactor);

        // Get the contactor
        restContactorMockMvc.perform(get("/api/contactors/{id}", contactor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contactor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactor() throws Exception {
        // Get the contactor
        restContactorMockMvc.perform(get("/api/contactors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactor() throws Exception {
        // Initialize the database
        contactorService.save(contactor);

        int databaseSizeBeforeUpdate = contactorRepository.findAll().size();

        // Update the contactor
        Contactor updatedContactor = contactorRepository.findById(contactor.getId()).get();
        // Disconnect from session so that the updates on updatedContactor are not directly saved in db
        em.detach(updatedContactor);
        updatedContactor
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE);

        restContactorMockMvc.perform(put("/api/contactors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactor)))
            .andExpect(status().isOk());

        // Validate the Contactor in the database
        List<Contactor> contactorList = contactorRepository.findAll();
        assertThat(contactorList).hasSize(databaseSizeBeforeUpdate);
        Contactor testContactor = contactorList.get(contactorList.size() - 1);
        assertThat(testContactor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactor.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingContactor() throws Exception {
        int databaseSizeBeforeUpdate = contactorRepository.findAll().size();

        // Create the Contactor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactorMockMvc.perform(put("/api/contactors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactor)))
            .andExpect(status().isBadRequest());

        // Validate the Contactor in the database
        List<Contactor> contactorList = contactorRepository.findAll();
        assertThat(contactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContactor() throws Exception {
        // Initialize the database
        contactorService.save(contactor);

        int databaseSizeBeforeDelete = contactorRepository.findAll().size();

        // Get the contactor
        restContactorMockMvc.perform(delete("/api/contactors/{id}", contactor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contactor> contactorList = contactorRepository.findAll();
        assertThat(contactorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contactor.class);
        Contactor contactor1 = new Contactor();
        contactor1.setId(1L);
        Contactor contactor2 = new Contactor();
        contactor2.setId(contactor1.getId());
        assertThat(contactor1).isEqualTo(contactor2);
        contactor2.setId(2L);
        assertThat(contactor1).isNotEqualTo(contactor2);
        contactor1.setId(null);
        assertThat(contactor1).isNotEqualTo(contactor2);
    }
}
