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

import net.jianbo.cmdb.domain.ComponentEntity;
import net.jianbo.cmdb.domain.*; // for static metamodels
import net.jianbo.cmdb.repository.ComponentEntityRepository;
import net.jianbo.cmdb.service.dto.ComponentEntityCriteria;

/**
 * Service for executing complex queries for ComponentEntity entities in the database.
 * The main input is a {@link ComponentEntityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ComponentEntity} or a {@link Page} of {@link ComponentEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComponentEntityQueryService extends QueryService<ComponentEntity> {

    private final Logger log = LoggerFactory.getLogger(ComponentEntityQueryService.class);

    private final ComponentEntityRepository componentEntityRepository;

    public ComponentEntityQueryService(ComponentEntityRepository componentEntityRepository) {
        this.componentEntityRepository = componentEntityRepository;
    }

    /**
     * Return a {@link List} of {@link ComponentEntity} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ComponentEntity> findByCriteria(ComponentEntityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ComponentEntity> specification = createSpecification(criteria);
        return componentEntityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ComponentEntity} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComponentEntity> findByCriteria(ComponentEntityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ComponentEntity> specification = createSpecification(criteria);
        return componentEntityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComponentEntityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ComponentEntity> specification = createSpecification(criteria);
        return componentEntityRepository.count(specification);
    }

    /**
     * Function to convert ComponentEntityCriteria to a {@link Specification}
     */
    private Specification<ComponentEntity> createSpecification(ComponentEntityCriteria criteria) {
        Specification<ComponentEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ComponentEntity_.id));
            }
            if (criteria.getComName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComName(), ComponentEntity_.comName));
            }
            if (criteria.getComType() != null) {
                specification = specification.and(buildSpecification(criteria.getComType(), ComponentEntity_.comType));
            }
            if (criteria.getVersionsId() != null) {
                specification = specification.and(buildSpecification(criteria.getVersionsId(),
                    root -> root.join(ComponentEntity_.versions, JoinType.LEFT).get(Version_.id)));
            }
            if (criteria.getAppId() != null) {
                specification = specification.and(buildSpecification(criteria.getAppId(),
                    root -> root.join(ComponentEntity_.app, JoinType.LEFT).get(Application_.id)));
            }
            if (criteria.getServerId() != null) {
                specification = specification.and(buildSpecification(criteria.getServerId(),
                    root -> root.join(ComponentEntity_.server, JoinType.LEFT).get(Server_.id)));
            }
        }
        return specification;
    }
}
