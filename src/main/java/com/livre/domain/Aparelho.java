package com.livre.domain;

import com.livre.domain.enumeration.Status;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Aparelho.
 */
@Entity
@Table(name = "aparelho")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Aparelho implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "numero_serie")
    private String numeroSerie;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "carga")
    private Integer carga;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Aparelho id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Aparelho nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroSerie() {
        return this.numeroSerie;
    }

    public Aparelho numeroSerie(String numeroSerie) {
        this.setNumeroSerie(numeroSerie);
        return this;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Status getStatus() {
        return this.status;
    }

    public Aparelho status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getCarga() {
        return this.carga;
    }

    public Aparelho carga(Integer carga) {
        this.setCarga(carga);
        return this;
    }

    public void setCarga(Integer carga) {
        this.carga = carga;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aparelho)) {
            return false;
        }
        return id != null && id.equals(((Aparelho) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Aparelho{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", numeroSerie='" + getNumeroSerie() + "'" +
            ", status='" + getStatus() + "'" +
            ", carga=" + getCarga() +
            "}";
    }
}
