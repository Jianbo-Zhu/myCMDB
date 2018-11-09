package net.jianbo.cmdb.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import net.jianbo.cmdb.domain.Contactor;
import net.jianbo.cmdb.domain.*; // for static metamodels
import net.jianbo.cmdb.repository.ContactorRepository;
import net.jianbo.cmdb.service.dto.ContactorCriteria;

/**
 * Service for executing complex queries for Contactor entities in the database.
 * The main input is a {@link ContactorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Contactor} or a {@link Page} of {@link Contactor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactorQueryService extends QueryService<Contactor> {

    private final Logger log = LoggerFactory.getLogger(ContactorQueryService.class);

    private final ContactorRepository contactorRepository;

    public ContactorQueryService(ContactorRepository contactorRepository) {
        this.contactorRepository = contactorRepository;
    }

    /**
     * Return a {@link List} of {@link Contactor} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Contactor> findByCriteria(ContactorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Contactor> specification = createSpecification(criteria);
        return contactorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Contactor} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Contactor> findByCriteria(ContactorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Contactor> specification = createSpecification(criteria);
        return contactorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Contactor> specification = createSpecification(criteria);
        return contactorRepository.count(specification);
    }

    /**
     * Function to convert ContactorCriteria to a {@link Specification}
     */
    private Specification<Contactor> createSpecification(ContactorCriteria criteria) {
        Specification<Contactor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Contactor_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Contactor_.name));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Contactor_.phone));
            }
        }
        return specification;
    }
}
