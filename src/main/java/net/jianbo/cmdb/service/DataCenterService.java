package net.jianbo.cmdb.service;

import net.jianbo.cmdb.domain.DataCenter;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing DataCenter.
 */
public interface DataCenterService {

    /**
     * Save a dataCenter.
     *
     * @param dataCenter the entity to save
     * @return the persisted entity
     */
    DataCenter save(DataCenter dataCenter);

    /**
     * Get all the dataCenters.
     *
     * @return the list of entities
     */
    List<DataCenter> findAll();


    /**
     * Get the "id" dataCenter.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DataCenter> findOne(Long id);

    /**
     * Delete the "id" dataCenter.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
