package net.jianbo.cmdb.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.jianbo.cmdb.domain.Server;
import net.jianbo.cmdb.service.ServerService;
import net.jianbo.cmdb.web.rest.errors.BadRequestAlertException;
import net.jianbo.cmdb.web.rest.util.HeaderUtil;
import net.jianbo.cmdb.service.dto.ServerCriteria;
import net.jianbo.cmdb.service.ServerQueryService;
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
 * REST controller for managing Server.
 */
@RestController
@RequestMapping("/api")
public class ServerResource {

    private final Logger log = LoggerFactory.getLogger(ServerResource.class);

    private static final String ENTITY_NAME = "server";

    private final ServerService serverService;

    private final ServerQueryService serverQueryService;

    public ServerResource(ServerService serverService, ServerQueryService serverQueryService) {
        this.serverService = serverService;
        this.serverQueryService = serverQueryService;
    }

    /**
     * POST  /servers : Create a new server.
     *
     * @param server the server to create
     * @return the ResponseEntity with status 201 (Created) and with body the new server, or with status 400 (Bad Request) if the server has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/servers")
    @Timed
    public ResponseEntity<Server> createServer(@Valid @RequestBody Server server) throws URISyntaxException {
        log.debug("REST request to save Server : {}", server);
        if (server.getId() != null) {
            throw new BadRequestAlertException("A new server cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Server result = serverService.save(server);
        return ResponseEntity.created(new URI("/api/servers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /servers : Updates an existing server.
     *
     * @param server the server to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated server,
     * or with status 400 (Bad Request) if the server is not valid,
     * or with status 500 (Internal Server Error) if the server couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/servers")
    @Timed
    public ResponseEntity<Server> updateServer(@Valid @RequestBody Server server) throws URISyntaxException {
        log.debug("REST request to update Server : {}", server);
        if (server.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Server result = serverService.save(server);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, server.getId().toString()))
            .body(result);
    }

    /**
     * GET  /servers : get all the servers.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of servers in body
     */
    @GetMapping("/servers")
    @Timed
    public ResponseEntity<List<Server>> getAllServers(ServerCriteria criteria) {
        log.debug("REST request to get Servers by criteria: {}", criteria);
        List<Server> entityList = serverQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /servers/count : count all the servers.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/servers/count")
    @Timed
    public ResponseEntity<Long> countServers(ServerCriteria criteria) {
        log.debug("REST request to count Servers by criteria: {}", criteria);
        return ResponseEntity.ok().body(serverQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /servers/:id : get the "id" server.
     *
     * @param id the id of the server to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the server, or with status 404 (Not Found)
     */
    @GetMapping("/servers/{id}")
    @Timed
    public ResponseEntity<Server> getServer(@PathVariable Long id) {
        log.debug("REST request to get Server : {}", id);
        Optional<Server> server = serverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(server);
    }

    /**
     * DELETE  /servers/:id : delete the "id" server.
     *
     * @param id the id of the server to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/servers/{id}")
    @Timed
    public ResponseEntity<Void> deleteServer(@PathVariable Long id) {
        log.debug("REST request to delete Server : {}", id);
        serverService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
