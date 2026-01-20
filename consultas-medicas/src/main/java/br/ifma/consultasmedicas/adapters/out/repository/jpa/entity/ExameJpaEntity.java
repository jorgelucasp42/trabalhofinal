package br.ifma.consultasmedicas.adapters.out.repository.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "exames")
public class ExameJpaEntity {

    @Id
    private Integer id;

    private String nome;

    protected ExameJpaEntity() {
        // JPA
    }

    public ExameJpaEntity(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
