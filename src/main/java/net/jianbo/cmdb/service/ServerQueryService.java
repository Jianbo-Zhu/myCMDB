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

import net.jianbo.cmdb.domain.Server;
import net.jianbo.cmdb.domain.*; // for static metamodels
import net.jianbo.cmdb.repository.ServerRepository;
import net.jianbo.cmdb.service.dto.ServerCriteria;

/**
 * Service for executing complex queries for Server entities in the database.
 * The main input is a {@link ServerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Server} or a {@link Page} of {@link Server} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServerQueryService extends QueryService<Server> {

    private final Logger log = LoggerFactory.getLogger(ServerQueryService.class);

    private final ServerRepository serverRepository;

    public ServerQueryService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    /**
     * Return a {@link List} of {@link Server} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Server> findByCriteria(ServerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Server> specification = createSpecification(criteria);
        return serverRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Server} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Server> findByCriteria(ServerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Server> specification = createSpecification(criteria);
        return serverRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Server> specification = createSpecification(criteria);
        return serverRepository.count(specification);
    }

    /**
     * Function to convert ServerCriteria to a {@link Specification}
     */
    private Specification<Server> createSpecification(ServerCriteria criteria) {
        Specification<Server> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Server_.id));
            }
            if (criteria.getHostname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHostname(), Server_.hostname));
            }
            if (criteria.getIpAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIpAddress(), Server_.ipAddress));
            }
            if (criteria.getMacAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMacAddress(), Server_.macAddress));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), Server_.position));
            }
            if (criteria.getBrand() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrand(), Server_.brand));
            }
            if (criteria.getMemSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMemSize(), Server_.memSize));
            }
            if (criteria.getCoreNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCoreNo(), Server_.coreNo));
            }
            if (criteria.getOsVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOsVersion(), Server_.osVersion));
            }
            if (criteria.getVendor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVendor(), Server_.vendor));
            }
            if (criteria.getPurchaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPurchaseDate(), Server_.purchaseDate));
            }
            if (criteria.getWarrantyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWarrantyDate(), Server_.warrantyDate));
            }
            if (criteria.getComponentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getComponentsId(),
                    root -> root.join(Server_.components, JoinType.LEFT).get(ComponentEntity_.id)));
            }
            if (criteria.getDataCenterId() != null) {
                specification = specification.and(buildSpecification(criteria.getDataCenterId(),
                    root -> root.join(Server_.dataCenter, JoinType.LEFT).get(DataCenter_.id)));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(Server_.owner, JoinType.LEFT).get(Contactor_.id)));
            }
            if (criteria.getVendorContactId() != null) {
                specification = specification.and(buildSpecification(criteria.getVendorContactId(),
                    root -> root.join(Server_.vendorContact, JoinType.LEFT).get(Contactor_.id)));
            }
        }
        return specification;
    }
}
