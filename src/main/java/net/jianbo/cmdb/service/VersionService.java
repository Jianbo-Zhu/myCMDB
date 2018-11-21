package net.jianbo.cmdb.service;

import net.jianbo.cmdb.domain.Version;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Version.
 */
public interface VersionService {

    /**
     * Save a version.
     *
     * @param version the entity to save
     * @return the persisted entity
     */
    Version save(Version version);

    /**
     * Get all the versions.
     *
     * @return the list of entities
     */
    List<Version> findAll();


    /**
     * Get the "id" version.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Version> findOne(Long id);

    /**
     * Delete the "id" version.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
