package net.jianbo.cmdb.service.impl;

import net.jianbo.cmdb.service.DataCenterService;
import net.jianbo.cmdb.domain.DataCenter;
import net.jianbo.cmdb.repository.DataCenterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing DataCenter.
 */
@Service
@Transactional
public class DataCenterServiceImpl implements DataCenterService {

    private final Logger log = LoggerFactory.getLogger(DataCenterServiceImpl.class);

    private final DataCenterRepository dataCenterRepository;

    public DataCenterServiceImpl(DataCenterRepository dataCenterRepository) {
        this.dataCenterRepository = dataCenterRepository;
    }

    /**
     * Save a dataCenter.
     *
     * @param dataCenter the entity to save
     * @return the persisted entity
     */
    @Override
    public DataCenter save(DataCenter dataCenter) {
        log.debug("Request to save DataCenter : {}", dataCenter);
        return dataCenterRepository.save(dataCenter);
    }

    /**
     * Get all the dataCenters.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<DataCenter> findAll() {
        log.debug("Request to get all DataCenters");
        return dataCenterRepository.findAll();
    }


    /**
     * Get one dataCenter by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DataCenter> findOne(Long id) {
        log.debug("Request to get DataCenter : {}", id);
        return dataCenterRepository.findById(id);
    }

    /**
     * Delete the dataCenter by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DataCenter : {}", id);
        dataCenterRepository.deleteById(id);
    }
}
