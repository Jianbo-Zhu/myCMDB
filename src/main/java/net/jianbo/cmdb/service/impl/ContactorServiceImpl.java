package net.jianbo.cmdb.service.impl;

import net.jianbo.cmdb.service.ContactorService;
import net.jianbo.cmdb.domain.Contactor;
import net.jianbo.cmdb.repository.ContactorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Contactor.
 */
@Service
@Transactional
public class ContactorServiceImpl implements ContactorService {

    private final Logger log = LoggerFactory.getLogger(ContactorServiceImpl.class);

    private final ContactorRepository contactorRepository;

    public ContactorServiceImpl(ContactorRepository contactorRepository) {
        this.contactorRepository = contactorRepository;
    }

    /**
     * Save a contactor.
     *
     * @param contactor the entity to save
     * @return the persisted entity
     */
    @Override
    public Contactor save(Contactor contactor) {
        log.debug("Request to save Contactor : {}", contactor);
        return contactorRepository.save(contactor);
    }

    /**
     * Get all the contactors.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Contactor> findAll() {
        log.debug("Request to get all Contactors");
        return contactorRepository.findAll();
    }


    /**
     * Get one contactor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Contactor> findOne(Long id) {
        log.debug("Request to get Contactor : {}", id);
        return contactorRepository.findById(id);
    }

    /**
     * Delete the contactor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contactor : {}", id);
        contactorRepository.deleteById(id);
    }
}
