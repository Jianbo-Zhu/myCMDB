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

import net.jianbo.cmdb.domain.Application;
import net.jianbo.cmdb.domain.*; // for static metamodels
import net.jianbo.cmdb.repository.ApplicationRepository;
import net.jianbo.cmdb.service.dto.ApplicationCriteria;

/**
 * Service for executing complex queries for Application entities in the database.
 * The main input is a {@link ApplicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Application} or a {@link Page} of {@link Application} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApplicationQueryService extends QueryService<Application> {

    private final Logger log = LoggerFactory.getLogger(ApplicationQueryService.class);

    private final ApplicationRepository applicationRepository;

    public ApplicationQueryService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    /**
     * Return a {@link List} of {@link Application} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Application> findByCriteria(ApplicationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Application> specification = createSpecification(criteria);
        return applicationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Application} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Application> findByCriteria(ApplicationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Application> specification = createSpecification(criteria);
        return applicationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApplicationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Application> specification = createSpecification(criteria);
        return applicationRepository.count(specification);
    }

    /**
     * Function to convert ApplicationCriteria to a {@link Specification}
     */
    private Specification<Application> createSpecification(ApplicationCriteria criteria) {
        Specification<Application> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Application_.id));
            }
            if (criteria.getAppName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAppName(), Application_.appName));
            }
            if (criteria.getEnvironment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEnvironment(), Application_.environment));
            }
            if (criteria.getComponentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getComponentsId(),
                    root -> root.join(Application_.components, JoinType.LEFT).get(ComponentEntity_.id)));
            }
        }
        return specification;
    }
}
