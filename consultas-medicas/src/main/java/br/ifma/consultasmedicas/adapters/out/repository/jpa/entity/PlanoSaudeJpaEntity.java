package br.ifma.consultasmedicas.adapters.out.repository.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "planos_saude")
public class PlanoSaudeJpaEntity {

    @Id
    private Integer id;

    @Column(name = "nome_plano", nullable = false)
    private String nomePlano;

    protected PlanoSaudeJpaEntity() {
        // JPA
    }

    public PlanoSaudeJpaEntity(Integer id, String nomePlano) {
        this.id = id;
        this.nomePlano = nomePlano;
    }

    public Integer getId() {
        return id;
    }

    public String getNomePlano() {
        return nomePlano;
    }
}
