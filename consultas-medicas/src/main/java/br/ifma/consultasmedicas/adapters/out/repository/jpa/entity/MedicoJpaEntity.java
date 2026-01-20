package br.ifma.consultasmedicas.adapters.out.repository.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medicos")
public class MedicoJpaEntity {

    @Id
    private Integer id;

    private String nome;
    private String especialidade;
    private String crm;

    protected MedicoJpaEntity() {}

    public MedicoJpaEntity(Integer id, String nome, String especialidade, String crm) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.crm = crm;
    }

    public Integer getId() { return id; }
    public String getNome() { return nome; }
    public String getEspecialidade() { return especialidade; }
    public String getCrm() { return crm; }
}
