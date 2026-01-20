package br.ifma.consultasmedicas.adapters.out.repository.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medicamentos")
public class MedicamentoJpaEntity {

    @Id
    private Integer id;

    private String nome;


    protected MedicamentoJpaEntity() {
        // JPA
    }

    public MedicamentoJpaEntity(Integer id, String nome) {
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

