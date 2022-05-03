package com.livre.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.livre.IntegrationTest;
import com.livre.domain.Aparelho;
import com.livre.domain.enumeration.Status;
import com.livre.repository.AparelhoRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AparelhoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AparelhoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_SERIE = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.EM_ESPERA;
    private static final Status UPDATED_STATUS = Status.EM_USO;

    private static final Integer DEFAULT_CARGA = 1;
    private static final Integer UPDATED_CARGA = 2;

    private static final String ENTITY_API_URL = "/api/aparelhos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AparelhoRepository aparelhoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAparelhoMockMvc;

    private Aparelho aparelho;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aparelho createEntity(EntityManager em) {
        Aparelho aparelho = new Aparelho().nome(DEFAULT_NOME).numeroSerie(DEFAULT_NUMERO_SERIE).status(DEFAULT_STATUS).carga(DEFAULT_CARGA);
        return aparelho;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aparelho createUpdatedEntity(EntityManager em) {
        Aparelho aparelho = new Aparelho().nome(UPDATED_NOME).numeroSerie(UPDATED_NUMERO_SERIE).status(UPDATED_STATUS).carga(UPDATED_CARGA);
        return aparelho;
    }

    @BeforeEach
    public void initTest() {
        aparelho = createEntity(em);
    }

    @Test
    @Transactional
    void createAparelho() throws Exception {
        int databaseSizeBeforeCreate = aparelhoRepository.findAll().size();
        // Create the Aparelho
        restAparelhoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aparelho)))
            .andExpect(status().isCreated());

        // Validate the Aparelho in the database
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeCreate + 1);
        Aparelho testAparelho = aparelhoList.get(aparelhoList.size() - 1);
        assertThat(testAparelho.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAparelho.getNumeroSerie()).isEqualTo(DEFAULT_NUMERO_SERIE);
        assertThat(testAparelho.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAparelho.getCarga()).isEqualTo(DEFAULT_CARGA);
    }

    @Test
    @Transactional
    void createAparelhoWithExistingId() throws Exception {
        // Create the Aparelho with an existing ID
        aparelho.setId(1L);

        int databaseSizeBeforeCreate = aparelhoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAparelhoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aparelho)))
            .andExpect(status().isBadRequest());

        // Validate the Aparelho in the database
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAparelhos() throws Exception {
        // Initialize the database
        aparelhoRepository.saveAndFlush(aparelho);

        // Get all the aparelhoList
        restAparelhoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aparelho.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].numeroSerie").value(hasItem(DEFAULT_NUMERO_SERIE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].carga").value(hasItem(DEFAULT_CARGA)));
    }

    @Test
    @Transactional
    void getAparelho() throws Exception {
        // Initialize the database
        aparelhoRepository.saveAndFlush(aparelho);

        // Get the aparelho
        restAparelhoMockMvc
            .perform(get(ENTITY_API_URL_ID, aparelho.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aparelho.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.numeroSerie").value(DEFAULT_NUMERO_SERIE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.carga").value(DEFAULT_CARGA));
    }

    @Test
    @Transactional
    void getNonExistingAparelho() throws Exception {
        // Get the aparelho
        restAparelhoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAparelho() throws Exception {
        // Initialize the database
        aparelhoRepository.saveAndFlush(aparelho);

        int databaseSizeBeforeUpdate = aparelhoRepository.findAll().size();

        // Update the aparelho
        Aparelho updatedAparelho = aparelhoRepository.findById(aparelho.getId()).get();
        // Disconnect from session so that the updates on updatedAparelho are not directly saved in db
        em.detach(updatedAparelho);
        updatedAparelho.nome(UPDATED_NOME).numeroSerie(UPDATED_NUMERO_SERIE).status(UPDATED_STATUS).carga(UPDATED_CARGA);

        restAparelhoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAparelho.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAparelho))
            )
            .andExpect(status().isOk());

        // Validate the Aparelho in the database
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeUpdate);
        Aparelho testAparelho = aparelhoList.get(aparelhoList.size() - 1);
        assertThat(testAparelho.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAparelho.getNumeroSerie()).isEqualTo(UPDATED_NUMERO_SERIE);
        assertThat(testAparelho.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAparelho.getCarga()).isEqualTo(UPDATED_CARGA);
    }

    @Test
    @Transactional
    void putNonExistingAparelho() throws Exception {
        int databaseSizeBeforeUpdate = aparelhoRepository.findAll().size();
        aparelho.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAparelhoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aparelho.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aparelho))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aparelho in the database
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAparelho() throws Exception {
        int databaseSizeBeforeUpdate = aparelhoRepository.findAll().size();
        aparelho.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAparelhoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aparelho))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aparelho in the database
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAparelho() throws Exception {
        int databaseSizeBeforeUpdate = aparelhoRepository.findAll().size();
        aparelho.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAparelhoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aparelho)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aparelho in the database
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAparelhoWithPatch() throws Exception {
        // Initialize the database
        aparelhoRepository.saveAndFlush(aparelho);

        int databaseSizeBeforeUpdate = aparelhoRepository.findAll().size();

        // Update the aparelho using partial update
        Aparelho partialUpdatedAparelho = new Aparelho();
        partialUpdatedAparelho.setId(aparelho.getId());

        partialUpdatedAparelho.numeroSerie(UPDATED_NUMERO_SERIE);

        restAparelhoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAparelho.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAparelho))
            )
            .andExpect(status().isOk());

        // Validate the Aparelho in the database
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeUpdate);
        Aparelho testAparelho = aparelhoList.get(aparelhoList.size() - 1);
        assertThat(testAparelho.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAparelho.getNumeroSerie()).isEqualTo(UPDATED_NUMERO_SERIE);
        assertThat(testAparelho.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAparelho.getCarga()).isEqualTo(DEFAULT_CARGA);
    }

    @Test
    @Transactional
    void fullUpdateAparelhoWithPatch() throws Exception {
        // Initialize the database
        aparelhoRepository.saveAndFlush(aparelho);

        int databaseSizeBeforeUpdate = aparelhoRepository.findAll().size();

        // Update the aparelho using partial update
        Aparelho partialUpdatedAparelho = new Aparelho();
        partialUpdatedAparelho.setId(aparelho.getId());

        partialUpdatedAparelho.nome(UPDATED_NOME).numeroSerie(UPDATED_NUMERO_SERIE).status(UPDATED_STATUS).carga(UPDATED_CARGA);

        restAparelhoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAparelho.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAparelho))
            )
            .andExpect(status().isOk());

        // Validate the Aparelho in the database
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeUpdate);
        Aparelho testAparelho = aparelhoList.get(aparelhoList.size() - 1);
        assertThat(testAparelho.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAparelho.getNumeroSerie()).isEqualTo(UPDATED_NUMERO_SERIE);
        assertThat(testAparelho.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAparelho.getCarga()).isEqualTo(UPDATED_CARGA);
    }

    @Test
    @Transactional
    void patchNonExistingAparelho() throws Exception {
        int databaseSizeBeforeUpdate = aparelhoRepository.findAll().size();
        aparelho.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAparelhoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aparelho.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aparelho))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aparelho in the database
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAparelho() throws Exception {
        int databaseSizeBeforeUpdate = aparelhoRepository.findAll().size();
        aparelho.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAparelhoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aparelho))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aparelho in the database
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAparelho() throws Exception {
        int databaseSizeBeforeUpdate = aparelhoRepository.findAll().size();
        aparelho.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAparelhoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aparelho)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aparelho in the database
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAparelho() throws Exception {
        // Initialize the database
        aparelhoRepository.saveAndFlush(aparelho);

        int databaseSizeBeforeDelete = aparelhoRepository.findAll().size();

        // Delete the aparelho
        restAparelhoMockMvc
            .perform(delete(ENTITY_API_URL_ID, aparelho.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Aparelho> aparelhoList = aparelhoRepository.findAll();
        assertThat(aparelhoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
