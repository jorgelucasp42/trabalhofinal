package br.ifma.consultasmedicas.adapters.out.repository.jpa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
public class PacienteJpaEntity {

    @Id
    private Integer id;

    private String nomeCrianca;
    private String nomeResponsavel;
    private LocalDate dataNascimento;
    private String sexo;

    @Embedded
    private EnderecoEmbeddable endereco;

    private String planoSaude;

    protected PacienteJpaEntity() {}

    public PacienteJpaEntity(
            Integer id,
            String nomeCrianca,
            String nomeResponsavel,
            LocalDate dataNascimento,
            String sexo,
            EnderecoEmbeddable endereco,
            String planoSaude
    ) {
        this.id = id;
        this.nomeCrianca = nomeCrianca;
        this.nomeResponsavel = nomeResponsavel;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.endereco = endereco;
        this.planoSaude = planoSaude;
    }

    public Integer getId() { return id; }
    public String getNomeCrianca() { return nomeCrianca; }
    public String getNomeResponsavel() { return nomeResponsavel; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getSexo() { return sexo; }
    public EnderecoEmbeddable getEndereco() { return endereco; }
    public String getPlanoSaude() { return planoSaude; }
}