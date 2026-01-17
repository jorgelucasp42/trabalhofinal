package br.ifma.consultasmedicas.adapters.out.repository.jpa.entity;

import jakarta.persistence.*;

/**
 * Entidade JPA para Prontuário.
 * Separada da entidade de domínio para não poluir o core.
 */
@Entity
@Table(name = "prontuarios")
public class ProntuarioJpaEntity {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "consulta_id", nullable = false)
    private Integer consultaId;

    @Column(name = "peso", nullable = false)
    private double peso;

    @Column(name = "altura", nullable = false)
    private double altura;

    @Column(name = "imc")
    private double imc;

    @Column(name = "sintomas", columnDefinition = "TEXT")
    private String sintomas;

    @Column(name = "observacao_clinica", columnDefinition = "TEXT")
    private String observacaoClinica;

    public ProntuarioJpaEntity() {
    }

    public ProntuarioJpaEntity(Integer id, Integer consultaId, double peso, double altura,
            double imc, String sintomas, String observacaoClinica) {
        this.id = id;
        this.consultaId = consultaId;
        this.peso = peso;
        this.altura = altura;
        this.imc = imc;
        this.sintomas = sintomas;
        this.observacaoClinica = observacaoClinica;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Integer consultaId) {
        this.consultaId = consultaId;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getObservacaoClinica() {
        return observacaoClinica;
    }

    public void setObservacaoClinica(String observacaoClinica) {
        this.observacaoClinica = observacaoClinica;
    }
}
