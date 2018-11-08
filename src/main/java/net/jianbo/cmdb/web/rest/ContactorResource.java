package net.jianbo.cmdb.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.jianbo.cmdb.domain.Contactor;
import net.jianbo.cmdb.service.ContactorService;
import net.jianbo.cmdb.web.rest.errors.BadRequestAlertException;
import net.jianbo.cmdb.web.rest.util.HeaderUtil;
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
 * REST controller for managing Contactor.
 */
@RestController
@RequestMapping("/api")
public class ContactorResource {

    private final Logger log = LoggerFactory.getLogger(ContactorResource.class);

    private static final String ENTITY_NAME = "contactor";

    private final ContactorService contactorService;

    public ContactorResource(ContactorService contactorService) {
        this.contactorService = contactorService;
    }

    /**
     * POST  /contactors : Create a new contactor.
     *
     * @param contactor the contactor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactor, or with status 400 (Bad Request) if the contactor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contactors")
    @Timed
    public ResponseEntity<Contactor> createContactor(@Valid @RequestBody Contactor contactor) throws URISyntaxException {
        log.debug("REST request to save Contactor : {}", contactor);
        if (contactor.getId() != null) {
            throw new BadRequestAlertException("A new contactor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contactor result = contactorService.save(contactor);
        return ResponseEntity.created(new URI("/api/contactors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contactors : Updates an existing contactor.
     *
     * @param contactor the contactor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactor,
     * or with status 400 (Bad Request) if the contactor is not valid,
     * or with status 500 (Internal Server Error) if the contactor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contactors")
    @Timed
    public ResponseEntity<Contactor> updateContactor(@Valid @RequestBody Contactor contactor) throws URISyntaxException {
        log.debug("REST request to update Contactor : {}", contactor);
        if (contactor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Contactor result = contactorService.save(contactor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contactors : get all the contactors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contactors in body
     */
    @GetMapping("/contactors")
    @Timed
    public List<Contactor> getAllContactors() {
        log.debug("REST request to get all Contactors");
        return contactorService.findAll();
    }

    /**
     * GET  /contactors/:id : get the "id" contactor.
     *
     * @param id the id of the contactor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactor, or with status 404 (Not Found)
     */
    @GetMapping("/contactors/{id}")
    @Timed
    public ResponseEntity<Contactor> getContactor(@PathVariable Long id) {
        log.debug("REST request to get Contactor : {}", id);
        Optional<Contactor> contactor = contactorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactor);
    }

    /**
     * DELETE  /contactors/:id : delete the "id" contactor.
     *
     * @param id the id of the contactor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contactors/{id}")
    @Timed
    public ResponseEntity<Void> deleteContactor(@PathVariable Long id) {
        log.debug("REST request to delete Contactor : {}", id);
        contactorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
