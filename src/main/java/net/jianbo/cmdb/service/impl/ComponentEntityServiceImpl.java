package net.jianbo.cmdb.service.impl;

import net.jianbo.cmdb.service.ComponentEntityService;
import net.jianbo.cmdb.domain.ComponentEntity;
import net.jianbo.cmdb.repository.ComponentEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing ComponentEntity.
 */
@Service
@Transactional
public class ComponentEntityServiceImpl implements ComponentEntityService {

    private final Logger log = LoggerFactory.getLogger(ComponentEntityServiceImpl.class);

    private final ComponentEntityRepository componentEntityRepository;

    public ComponentEntityServiceImpl(ComponentEntityRepository componentEntityRepository) {
        this.componentEntityRepository = componentEntityRepository;
    }

    /**
     * Save a componentEntity.
     *
     * @param componentEntity the entity to save
     * @return the persisted entity
     */
    @Override
    public ComponentEntity save(ComponentEntity componentEntity) {
        log.debug("Request to save ComponentEntity : {}", componentEntity);
        return componentEntityRepository.save(componentEntity);
    }

    /**
     * Get all the componentEntities.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ComponentEntity> findAll() {
        log.debug("Request to get all ComponentEntities");
        return componentEntityRepository.findAll();
    }


    /**
     * Get one componentEntity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ComponentEntity> findOne(Long id) {
        log.debug("Request to get ComponentEntity : {}", id);
        return componentEntityRepository.findById(id);
    }

    /**
     * Delete the componentEntity by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ComponentEntity : {}", id);
        componentEntityRepository.deleteById(id);
    }
}
