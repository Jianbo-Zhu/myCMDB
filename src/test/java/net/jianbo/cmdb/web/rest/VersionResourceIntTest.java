package net.jianbo.cmdb.web.rest;

import net.jianbo.cmdb.MyCmdbApp;

import net.jianbo.cmdb.domain.Version;
import net.jianbo.cmdb.domain.ComponentEntity;
import net.jianbo.cmdb.repository.VersionRepository;
import net.jianbo.cmdb.service.VersionService;
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
 * Test class for the VersionResource REST controller.
 *
 * @see VersionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyCmdbApp.class)
public class VersionResourceIntTest {

    private static final String DEFAULT_VERSION_STRING = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_STRING = "BBBBBBBBBB";

    private static final String DEFAULT_DEPLOYED_BY = "AAAAAAAAAA";
    private static final String UPDATED_DEPLOYED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_GIT_COMMIT = "AAAAAAAAAA";
    private static final String UPDATED_GIT_COMMIT = "BBBBBBBBBB";

    private static final String DEFAULT_GIT_COMMITTER = "AAAAAAAAAA";
    private static final String UPDATED_GIT_COMMITTER = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAJOR_VERSION = 1;
    private static final Integer UPDATED_MAJOR_VERSION = 2;

    private static final Integer DEFAULT_MINOR_VERSION = 1;
    private static final Integer UPDATED_MINOR_VERSION = 2;

    private static final Integer DEFAULT_HOTFIX_NUMBER = 1;
    private static final Integer UPDATED_HOTFIX_NUMBER = 2;

    private static final Integer DEFAULT_BUILD_NUMBER = 1;
    private static final Integer UPDATED_BUILD_NUMBER = 2;

    @Autowired
    private VersionRepository versionRepository;

    @Autowired
    private VersionService versionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVersionMockMvc;

    private Version version;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VersionResource versionResource = new VersionResource(versionService);
        this.restVersionMockMvc = MockMvcBuilders.standaloneSetup(versionResource)
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
    public static Version createEntity(EntityManager em) {
        Version version = new Version()
            .versionString(DEFAULT_VERSION_STRING)
            .deployedBy(DEFAULT_DEPLOYED_BY)
            .gitCommit(DEFAULT_GIT_COMMIT)
            .gitCommitter(DEFAULT_GIT_COMMITTER)
            .majorVersion(DEFAULT_MAJOR_VERSION)
            .minorVersion(DEFAULT_MINOR_VERSION)
            .hotfixNumber(DEFAULT_HOTFIX_NUMBER)
            .buildNumber(DEFAULT_BUILD_NUMBER);
        // Add required entity
        ComponentEntity componentEntity = ComponentEntityResourceIntTest.createEntity(em);
        em.persist(componentEntity);
        em.flush();
        version.setComp(componentEntity);
        return version;
    }

    @Before
    public void initTest() {
        version = createEntity(em);
    }

    @Test
    @Transactional
    public void createVersion() throws Exception {
        int databaseSizeBeforeCreate = versionRepository.findAll().size();

        // Create the Version
        restVersionMockMvc.perform(post("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(version)))
            .andExpect(status().isCreated());

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeCreate + 1);
        Version testVersion = versionList.get(versionList.size() - 1);
        assertThat(testVersion.getVersionString()).isEqualTo(DEFAULT_VERSION_STRING);
        assertThat(testVersion.getDeployedBy()).isEqualTo(DEFAULT_DEPLOYED_BY);
        assertThat(testVersion.getGitCommit()).isEqualTo(DEFAULT_GIT_COMMIT);
        assertThat(testVersion.getGitCommitter()).isEqualTo(DEFAULT_GIT_COMMITTER);
        assertThat(testVersion.getMajorVersion()).isEqualTo(DEFAULT_MAJOR_VERSION);
        assertThat(testVersion.getMinorVersion()).isEqualTo(DEFAULT_MINOR_VERSION);
        assertThat(testVersion.getHotfixNumber()).isEqualTo(DEFAULT_HOTFIX_NUMBER);
        assertThat(testVersion.getBuildNumber()).isEqualTo(DEFAULT_BUILD_NUMBER);
    }

    @Test
    @Transactional
    public void createVersionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = versionRepository.findAll().size();

        // Create the Version with an existing ID
        version.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVersionMockMvc.perform(post("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(version)))
            .andExpect(status().isBadRequest());

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkVersionStringIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionRepository.findAll().size();
        // set the field null
        version.setVersionString(null);

        // Create the Version, which fails.

        restVersionMockMvc.perform(post("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(version)))
            .andExpect(status().isBadRequest());

        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeployedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionRepository.findAll().size();
        // set the field null
        version.setDeployedBy(null);

        // Create the Version, which fails.

        restVersionMockMvc.perform(post("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(version)))
            .andExpect(status().isBadRequest());

        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGitCommitIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionRepository.findAll().size();
        // set the field null
        version.setGitCommit(null);

        // Create the Version, which fails.

        restVersionMockMvc.perform(post("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(version)))
            .andExpect(status().isBadRequest());

        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGitCommitterIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionRepository.findAll().size();
        // set the field null
        version.setGitCommitter(null);

        // Create the Version, which fails.

        restVersionMockMvc.perform(post("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(version)))
            .andExpect(status().isBadRequest());

        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMajorVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionRepository.findAll().size();
        // set the field null
        version.setMajorVersion(null);

        // Create the Version, which fails.

        restVersionMockMvc.perform(post("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(version)))
            .andExpect(status().isBadRequest());

        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinorVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionRepository.findAll().size();
        // set the field null
        version.setMinorVersion(null);

        // Create the Version, which fails.

        restVersionMockMvc.perform(post("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(version)))
            .andExpect(status().isBadRequest());

        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHotfixNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionRepository.findAll().size();
        // set the field null
        version.setHotfixNumber(null);

        // Create the Version, which fails.

        restVersionMockMvc.perform(post("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(version)))
            .andExpect(status().isBadRequest());

        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBuildNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionRepository.findAll().size();
        // set the field null
        version.setBuildNumber(null);

        // Create the Version, which fails.

        restVersionMockMvc.perform(post("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(version)))
            .andExpect(status().isBadRequest());

        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVersions() throws Exception {
        // Initialize the database
        versionRepository.saveAndFlush(version);

        // Get all the versionList
        restVersionMockMvc.perform(get("/api/versions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(version.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionString").value(hasItem(DEFAULT_VERSION_STRING.toString())))
            .andExpect(jsonPath("$.[*].deployedBy").value(hasItem(DEFAULT_DEPLOYED_BY.toString())))
            .andExpect(jsonPath("$.[*].gitCommit").value(hasItem(DEFAULT_GIT_COMMIT.toString())))
            .andExpect(jsonPath("$.[*].gitCommitter").value(hasItem(DEFAULT_GIT_COMMITTER.toString())))
            .andExpect(jsonPath("$.[*].majorVersion").value(hasItem(DEFAULT_MAJOR_VERSION)))
            .andExpect(jsonPath("$.[*].minorVersion").value(hasItem(DEFAULT_MINOR_VERSION)))
            .andExpect(jsonPath("$.[*].hotfixNumber").value(hasItem(DEFAULT_HOTFIX_NUMBER)))
            .andExpect(jsonPath("$.[*].buildNumber").value(hasItem(DEFAULT_BUILD_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getVersion() throws Exception {
        // Initialize the database
        versionRepository.saveAndFlush(version);

        // Get the version
        restVersionMockMvc.perform(get("/api/versions/{id}", version.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(version.getId().intValue()))
            .andExpect(jsonPath("$.versionString").value(DEFAULT_VERSION_STRING.toString()))
            .andExpect(jsonPath("$.deployedBy").value(DEFAULT_DEPLOYED_BY.toString()))
            .andExpect(jsonPath("$.gitCommit").value(DEFAULT_GIT_COMMIT.toString()))
            .andExpect(jsonPath("$.gitCommitter").value(DEFAULT_GIT_COMMITTER.toString()))
            .andExpect(jsonPath("$.majorVersion").value(DEFAULT_MAJOR_VERSION))
            .andExpect(jsonPath("$.minorVersion").value(DEFAULT_MINOR_VERSION))
            .andExpect(jsonPath("$.hotfixNumber").value(DEFAULT_HOTFIX_NUMBER))
            .andExpect(jsonPath("$.buildNumber").value(DEFAULT_BUILD_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingVersion() throws Exception {
        // Get the version
        restVersionMockMvc.perform(get("/api/versions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVersion() throws Exception {
        // Initialize the database
        versionService.save(version);

        int databaseSizeBeforeUpdate = versionRepository.findAll().size();

        // Update the version
        Version updatedVersion = versionRepository.findById(version.getId()).get();
        // Disconnect from session so that the updates on updatedVersion are not directly saved in db
        em.detach(updatedVersion);
        updatedVersion
            .versionString(UPDATED_VERSION_STRING)
            .deployedBy(UPDATED_DEPLOYED_BY)
            .gitCommit(UPDATED_GIT_COMMIT)
            .gitCommitter(UPDATED_GIT_COMMITTER)
            .majorVersion(UPDATED_MAJOR_VERSION)
            .minorVersion(UPDATED_MINOR_VERSION)
            .hotfixNumber(UPDATED_HOTFIX_NUMBER)
            .buildNumber(UPDATED_BUILD_NUMBER);

        restVersionMockMvc.perform(put("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVersion)))
            .andExpect(status().isOk());

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate);
        Version testVersion = versionList.get(versionList.size() - 1);
        assertThat(testVersion.getVersionString()).isEqualTo(UPDATED_VERSION_STRING);
        assertThat(testVersion.getDeployedBy()).isEqualTo(UPDATED_DEPLOYED_BY);
        assertThat(testVersion.getGitCommit()).isEqualTo(UPDATED_GIT_COMMIT);
        assertThat(testVersion.getGitCommitter()).isEqualTo(UPDATED_GIT_COMMITTER);
        assertThat(testVersion.getMajorVersion()).isEqualTo(UPDATED_MAJOR_VERSION);
        assertThat(testVersion.getMinorVersion()).isEqualTo(UPDATED_MINOR_VERSION);
        assertThat(testVersion.getHotfixNumber()).isEqualTo(UPDATED_HOTFIX_NUMBER);
        assertThat(testVersion.getBuildNumber()).isEqualTo(UPDATED_BUILD_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingVersion() throws Exception {
        int databaseSizeBeforeUpdate = versionRepository.findAll().size();

        // Create the Version

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVersionMockMvc.perform(put("/api/versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(version)))
            .andExpect(status().isBadRequest());

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVersion() throws Exception {
        // Initialize the database
        versionService.save(version);

        int databaseSizeBeforeDelete = versionRepository.findAll().size();

        // Get the version
        restVersionMockMvc.perform(delete("/api/versions/{id}", version.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Version> versionList = versionRepository.findAll();
        assertThat(versionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Version.class);
        Version version1 = new Version();
        version1.setId(1L);
        Version version2 = new Version();
        version2.setId(version1.getId());
        assertThat(version1).isEqualTo(version2);
        version2.setId(2L);
        assertThat(version1).isNotEqualTo(version2);
        version1.setId(null);
        assertThat(version1).isNotEqualTo(version2);
    }
}
