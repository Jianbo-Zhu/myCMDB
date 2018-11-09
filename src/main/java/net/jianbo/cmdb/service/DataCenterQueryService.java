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

import net.jianbo.cmdb.domain.DataCenter;
import net.jianbo.cmdb.domain.*; // for static metamodels
import net.jianbo.cmdb.repository.DataCenterRepository;
import net.jianbo.cmdb.service.dto.DataCenterCriteria;

/**
 * Service for executing complex queries for DataCenter entities in the database.
 * The main input is a {@link DataCenterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DataCenter} or a {@link Page} of {@link DataCenter} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DataCenterQueryService extends QueryService<DataCenter> {

    private final Logger log = LoggerFactory.getLogger(DataCenterQueryService.class);

    private final DataCenterRepository dataCenterRepository;

    public DataCenterQueryService(DataCenterRepository dataCenterRepository) {
        this.dataCenterRepository = dataCenterRepository;
    }

    /**
     * Return a {@link List} of {@link DataCenter} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataCenter> findByCriteria(DataCenterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DataCenter> specification = createSpecification(criteria);
        return dataCenterRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DataCenter} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DataCenter> findByCriteria(DataCenterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DataCenter> specification = createSpecification(criteria);
        return dataCenterRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DataCenterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DataCenter> specification = createSpecification(criteria);
        return dataCenterRepository.count(specification);
    }

    /**
     * Function to convert DataCenterCriteria to a {@link Specification}
     */
    private Specification<DataCenter> createSpecification(DataCenterCriteria criteria) {
        Specification<DataCenter> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DataCenter_.id));
            }
            if (criteria.getDcName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDcName(), DataCenter_.dcName));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), DataCenter_.address));
            }
            if (criteria.getContactorId() != null) {
                specification = specification.and(buildSpecification(criteria.getContactorId(),
                    root -> root.join(DataCenter_.contactor, JoinType.LEFT).get(Contactor_.id)));
            }
        }
        return specification;
    }
}
