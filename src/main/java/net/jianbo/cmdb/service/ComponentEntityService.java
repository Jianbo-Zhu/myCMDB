package net.jianbo.cmdb.service;

import net.jianbo.cmdb.domain.ComponentEntity;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ComponentEntity.
 */
public interface ComponentEntityService {

    /**
     * Save a componentEntity.
     *
     * @param componentEntity the entity to save
     * @return the persisted entity
     */
    ComponentEntity save(ComponentEntity componentEntity);

    /**
     * Get all the componentEntities.
     *
     * @return the list of entities
     */
    List<ComponentEntity> findAll();


    /**
     * Get the "id" componentEntity.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ComponentEntity> findOne(Long id);

    /**
     * Delete the "id" componentEntity.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
