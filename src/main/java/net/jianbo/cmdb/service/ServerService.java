package net.jianbo.cmdb.service;

import net.jianbo.cmdb.domain.Server;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Server.
 */
public interface ServerService {

    /**
     * Save a server.
     *
     * @param server the entity to save
     * @return the persisted entity
     */
    Server save(Server server);

    /**
     * Get all the servers.
     *
     * @return the list of entities
     */
    List<Server> findAll();


    /**
     * Get the "id" server.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Server> findOne(Long id);

    /**
     * Delete the "id" server.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
