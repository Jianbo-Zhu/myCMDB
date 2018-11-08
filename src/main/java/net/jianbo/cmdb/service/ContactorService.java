package net.jianbo.cmdb.service;

import net.jianbo.cmdb.domain.Contactor;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Contactor.
 */
public interface ContactorService {

    /**
     * Save a contactor.
     *
     * @param contactor the entity to save
     * @return the persisted entity
     */
    Contactor save(Contactor contactor);

    /**
     * Get all the contactors.
     *
     * @return the list of entities
     */
    List<Contactor> findAll();


    /**
     * Get the "id" contactor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Contactor> findOne(Long id);

    /**
     * Delete the "id" contactor.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
