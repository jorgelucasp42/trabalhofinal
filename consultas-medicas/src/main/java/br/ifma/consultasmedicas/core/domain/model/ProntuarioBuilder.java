package br.ifma.consultasmedicas.core.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder para construção de Prontuário (padrão sugerido no enunciado).
 */
public class ProntuarioBuilder {
    private Integer id;
    private Consulta consulta;
    private double peso;
    private double altura;
    private String sintomas;
    private String observacaoClinica;
    private final List<Prescricao> prescricoes = new ArrayList<>();
    private final List<Exame> exames = new ArrayList<>();

    private ProntuarioBuilder() {}

    public static ProntuarioBuilder builder() {
        return new ProntuarioBuilder();
    }

    public ProntuarioBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public ProntuarioBuilder consulta(Consulta consulta) {
        this.consulta = consulta;
        return this;
    }

    public ProntuarioBuilder peso(double peso) {
        this.peso = peso;
        return this;
    }

    public ProntuarioBuilder altura(double altura) {
        this.altura = altura;
        return this;
    }

    public ProntuarioBuilder sintomas(String sintomas) {
        this.sintomas = sintomas;
        return this;
    }

    public ProntuarioBuilder observacao(String observacaoClinica) {
        this.observacaoClinica = observacaoClinica;
        return this;
    }

    public ProntuarioBuilder addPrescricao(Prescricao prescricao) {
        this.prescricoes.add(prescricao);
        return this;
    }

    public ProntuarioBuilder addExame(Exame exame) {
        this.exames.add(exame);
        return this;
    }

    public Prontuario build() {
        return new Prontuario(id, consulta, peso, altura, sintomas, observacaoClinica, prescricoes, exames);
    }
}
