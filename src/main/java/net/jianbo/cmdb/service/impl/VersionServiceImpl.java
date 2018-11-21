package net.jianbo.cmdb.service.impl;

import net.jianbo.cmdb.service.VersionService;
import net.jianbo.cmdb.domain.Version;
import net.jianbo.cmdb.repository.VersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Version.
 */
@Service
@Transactional
public class VersionServiceImpl implements VersionService {

    private final Logger log = LoggerFactory.getLogger(VersionServiceImpl.class);

    private final VersionRepository versionRepository;

    public VersionServiceImpl(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    /**
     * Save a version.
     *
     * @param version the entity to save
     * @return the persisted entity
     */
    @Override
    public Version save(Version version) {
        log.debug("Request to save Version : {}", version);
        return versionRepository.save(version);
    }

    /**
     * Get all the versions.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Version> findAll() {
        log.debug("Request to get all Versions");
        return versionRepository.findAll();
    }


    /**
     * Get one version by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Version> findOne(Long id) {
        log.debug("Request to get Version : {}", id);
        return versionRepository.findById(id);
    }

    /**
     * Delete the version by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Version : {}", id);
        versionRepository.deleteById(id);
    }
}
