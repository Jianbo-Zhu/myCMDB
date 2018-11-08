package net.jianbo.cmdb.service.impl;

import net.jianbo.cmdb.service.ApplicationService;
import net.jianbo.cmdb.domain.Application;
import net.jianbo.cmdb.repository.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Application.
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    /**
     * Save a application.
     *
     * @param application the entity to save
     * @return the persisted entity
     */
    @Override
    public Application save(Application application) {
        log.debug("Request to save Application : {}", application);
        return applicationRepository.save(application);
    }

    /**
     * Get all the applications.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Application> findAll() {
        log.debug("Request to get all Applications");
        return applicationRepository.findAll();
    }


    /**
     * Get one application by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Application> findOne(Long id) {
        log.debug("Request to get Application : {}", id);
        return applicationRepository.findById(id);
    }

    /**
     * Delete the application by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Application : {}", id);
        applicationRepository.deleteById(id);
    }
}
