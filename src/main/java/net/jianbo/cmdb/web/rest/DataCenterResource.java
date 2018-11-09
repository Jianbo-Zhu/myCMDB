package net.jianbo.cmdb.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.jianbo.cmdb.domain.DataCenter;
import net.jianbo.cmdb.service.DataCenterService;
import net.jianbo.cmdb.web.rest.errors.BadRequestAlertException;
import net.jianbo.cmdb.web.rest.util.HeaderUtil;
import net.jianbo.cmdb.service.dto.DataCenterCriteria;
import net.jianbo.cmdb.service.DataCenterQueryService;
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
 * REST controller for managing DataCenter.
 */
@RestController
@RequestMapping("/api")
public class DataCenterResource {

    private final Logger log = LoggerFactory.getLogger(DataCenterResource.class);

    private static final String ENTITY_NAME = "dataCenter";

    private final DataCenterService dataCenterService;

    private final DataCenterQueryService dataCenterQueryService;

    public DataCenterResource(DataCenterService dataCenterService, DataCenterQueryService dataCenterQueryService) {
        this.dataCenterService = dataCenterService;
        this.dataCenterQueryService = dataCenterQueryService;
    }

    /**
     * POST  /data-centers : Create a new dataCenter.
     *
     * @param dataCenter the dataCenter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataCenter, or with status 400 (Bad Request) if the dataCenter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-centers")
    @Timed
    public ResponseEntity<DataCenter> createDataCenter(@Valid @RequestBody DataCenter dataCenter) throws URISyntaxException {
        log.debug("REST request to save DataCenter : {}", dataCenter);
        if (dataCenter.getId() != null) {
            throw new BadRequestAlertException("A new dataCenter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataCenter result = dataCenterService.save(dataCenter);
        return ResponseEntity.created(new URI("/api/data-centers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-centers : Updates an existing dataCenter.
     *
     * @param dataCenter the dataCenter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataCenter,
     * or with status 400 (Bad Request) if the dataCenter is not valid,
     * or with status 500 (Internal Server Error) if the dataCenter couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-centers")
    @Timed
    public ResponseEntity<DataCenter> updateDataCenter(@Valid @RequestBody DataCenter dataCenter) throws URISyntaxException {
        log.debug("REST request to update DataCenter : {}", dataCenter);
        if (dataCenter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataCenter result = dataCenterService.save(dataCenter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataCenter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-centers : get all the dataCenters.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of dataCenters in body
     */
    @GetMapping("/data-centers")
    @Timed
    public ResponseEntity<List<DataCenter>> getAllDataCenters(DataCenterCriteria criteria) {
        log.debug("REST request to get DataCenters by criteria: {}", criteria);
        List<DataCenter> entityList = dataCenterQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /data-centers/count : count all the dataCenters.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/data-centers/count")
    @Timed
    public ResponseEntity<Long> countDataCenters(DataCenterCriteria criteria) {
        log.debug("REST request to count DataCenters by criteria: {}", criteria);
        return ResponseEntity.ok().body(dataCenterQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /data-centers/:id : get the "id" dataCenter.
     *
     * @param id the id of the dataCenter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataCenter, or with status 404 (Not Found)
     */
    @GetMapping("/data-centers/{id}")
    @Timed
    public ResponseEntity<DataCenter> getDataCenter(@PathVariable Long id) {
        log.debug("REST request to get DataCenter : {}", id);
        Optional<DataCenter> dataCenter = dataCenterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataCenter);
    }

    /**
     * DELETE  /data-centers/:id : delete the "id" dataCenter.
     *
     * @param id the id of the dataCenter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-centers/{id}")
    @Timed
    public ResponseEntity<Void> deleteDataCenter(@PathVariable Long id) {
        log.debug("REST request to delete DataCenter : {}", id);
        dataCenterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
