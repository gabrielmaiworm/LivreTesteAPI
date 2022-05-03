package com.livre.web.rest;

import com.livre.domain.Aparelho;
import com.livre.repository.AparelhoRepository;
import com.livre.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.livre.domain.Aparelho}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AparelhoResource {

    private final Logger log = LoggerFactory.getLogger(AparelhoResource.class);

    private static final String ENTITY_NAME = "aparelho";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AparelhoRepository aparelhoRepository;

    public AparelhoResource(AparelhoRepository aparelhoRepository) {
        this.aparelhoRepository = aparelhoRepository;
    }

    /**
     * {@code POST  /aparelhos} : Create a new aparelho.
     *
     * @param aparelho the aparelho to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aparelho, or with status {@code 400 (Bad Request)} if the aparelho has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aparelhos")
    public ResponseEntity<Aparelho> createAparelho(@RequestBody Aparelho aparelho) throws URISyntaxException {
        log.debug("REST request to save Aparelho : {}", aparelho);
        if (aparelho.getId() != null) {
            throw new BadRequestAlertException("A new aparelho cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Aparelho result = aparelhoRepository.save(aparelho);
        return ResponseEntity
            .created(new URI("/api/aparelhos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aparelhos/:id} : Updates an existing aparelho.
     *
     * @param id the id of the aparelho to save.
     * @param aparelho the aparelho to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aparelho,
     * or with status {@code 400 (Bad Request)} if the aparelho is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aparelho couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aparelhos/{id}")
    public ResponseEntity<Aparelho> updateAparelho(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Aparelho aparelho
    ) throws URISyntaxException {
        log.debug("REST request to update Aparelho : {}, {}", id, aparelho);
        if (aparelho.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aparelho.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aparelhoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Aparelho result = aparelhoRepository.save(aparelho);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aparelho.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aparelhos/:id} : Partial updates given fields of an existing aparelho, field will ignore if it is null
     *
     * @param id the id of the aparelho to save.
     * @param aparelho the aparelho to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aparelho,
     * or with status {@code 400 (Bad Request)} if the aparelho is not valid,
     * or with status {@code 404 (Not Found)} if the aparelho is not found,
     * or with status {@code 500 (Internal Server Error)} if the aparelho couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aparelhos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Aparelho> partialUpdateAparelho(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Aparelho aparelho
    ) throws URISyntaxException {
        log.debug("REST request to partial update Aparelho partially : {}, {}", id, aparelho);
        if (aparelho.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aparelho.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aparelhoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Aparelho> result = aparelhoRepository
            .findById(aparelho.getId())
            .map(existingAparelho -> {
                if (aparelho.getNome() != null) {
                    existingAparelho.setNome(aparelho.getNome());
                }
                if (aparelho.getNumeroSerie() != null) {
                    existingAparelho.setNumeroSerie(aparelho.getNumeroSerie());
                }
                if (aparelho.getStatus() != null) {
                    existingAparelho.setStatus(aparelho.getStatus());
                }
                if (aparelho.getCarga() != null) {
                    existingAparelho.setCarga(aparelho.getCarga());
                }

                return existingAparelho;
            })
            .map(aparelhoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aparelho.getId().toString())
        );
    }

    /**
     * {@code GET  /aparelhos} : get all the aparelhos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aparelhos in body.
     */
    @GetMapping("/aparelhos")
    public List<Aparelho> getAllAparelhos() {
        log.debug("REST request to get all Aparelhos");
        return aparelhoRepository.findAll();
    }

    /**
     * {@code GET  /aparelhos/:id} : get the "id" aparelho.
     *
     * @param id the id of the aparelho to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aparelho, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aparelhos/{id}")
    public ResponseEntity<Aparelho> getAparelho(@PathVariable Long id) {
        log.debug("REST request to get Aparelho : {}", id);
        Optional<Aparelho> aparelho = aparelhoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aparelho);
    }

    /**
     * {@code DELETE  /aparelhos/:id} : delete the "id" aparelho.
     *
     * @param id the id of the aparelho to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aparelhos/{id}")
    public ResponseEntity<Void> deleteAparelho(@PathVariable Long id) {
        log.debug("REST request to delete Aparelho : {}", id);
        aparelhoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
