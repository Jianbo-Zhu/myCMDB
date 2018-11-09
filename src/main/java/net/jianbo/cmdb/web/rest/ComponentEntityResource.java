package net.jianbo.cmdb.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.jianbo.cmdb.domain.ComponentEntity;
import net.jianbo.cmdb.service.ComponentEntityService;
import net.jianbo.cmdb.web.rest.errors.BadRequestAlertException;
import net.jianbo.cmdb.web.rest.util.HeaderUtil;
import net.jianbo.cmdb.service.dto.ComponentEntityCriteria;
import net.jianbo.cmdb.service.ComponentEntityQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ComponentEntity.
 */
@RestController
@RequestMapping("/api")
public class ComponentEntityResource {

    private final Logger log = LoggerFactory.getLogger(ComponentEntityResource.class);

    private static final String ENTITY_NAME = "componentEntity";

    private final ComponentEntityService componentEntityService;

    private final ComponentEntityQueryService componentEntityQueryService;

    public ComponentEntityResource(ComponentEntityService componentEntityService, ComponentEntityQueryService componentEntityQueryService) {
        this.componentEntityService = componentEntityService;
        this.componentEntityQueryService = componentEntityQueryService;
    }

    /**
     * POST  /component-entities : Create a new componentEntity.
     *
     * @param componentEntity the componentEntity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new componentEntity, or with status 400 (Bad Request) if the componentEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/component-entities")
    @Timed
    public ResponseEntity<ComponentEntity> createComponentEntity(@Valid @RequestBody ComponentEntity componentEntity) throws URISyntaxException {
        log.debug("REST request to save ComponentEntity : {}", componentEntity);
        if (componentEntity.getId() != null) {
            throw new BadRequestAlertException("A new componentEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComponentEntity result = componentEntityService.save(componentEntity);
        return ResponseEntity.created(new URI("/api/component-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /component-entities : Updates an existing componentEntity.
     *
     * @param componentEntity the componentEntity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated componentEntity,
     * or with status 400 (Bad Request) if the componentEntity is not valid,
     * or with status 500 (Internal Server Error) if the componentEntity couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/component-entities")
    @Timed
    public ResponseEntity<ComponentEntity> updateComponentEntity(@Valid @RequestBody ComponentEntity componentEntity) throws URISyntaxException {
        log.debug("REST request to update ComponentEntity : {}", componentEntity);
        if (componentEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ComponentEntity result = componentEntityService.save(componentEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, componentEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /component-entities : get all the componentEntities.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of componentEntities in body
     */
    @GetMapping("/component-entities")
    @Timed
    public ResponseEntity<List<ComponentEntity>> getAllComponentEntities(ComponentEntityCriteria criteria) {
        log.debug("REST request to get ComponentEntities by criteria: {}", criteria);
        List<ComponentEntity> entityList = componentEntityQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /component-entities/count : count all the componentEntities.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/component-entities/count")
    @Timed
    public ResponseEntity<Long> countComponentEntities(ComponentEntityCriteria criteria) {
        log.debug("REST request to count ComponentEntities by criteria: {}", criteria);
        return ResponseEntity.ok().body(componentEntityQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /component-entities/:id : get the "id" componentEntity.
     *
     * @param id the id of the componentEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the componentEntity, or with status 404 (Not Found)
     */
    @GetMapping("/component-entities/{id}")
    @Timed
    public ResponseEntity<ComponentEntity> getComponentEntity(@PathVariable Long id) {
        log.debug("REST request to get ComponentEntity : {}", id);
        Optional<ComponentEntity> componentEntity = componentEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(componentEntity);
    }

    /**
     * DELETE  /component-entities/:id : delete the "id" componentEntity.
     *
     * @param id the id of the componentEntity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/component-entities/{id}")
    @Timed
    public ResponseEntity<Void> deleteComponentEntity(@PathVariable Long id) {
        log.debug("REST request to delete ComponentEntity : {}", id);
        componentEntityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
