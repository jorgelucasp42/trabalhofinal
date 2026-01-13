package br.ifma.consultasmedicas.core.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Builder para construção de Prontuário (padrão sugerido no enunciado).
 * Responsável por construir Prontuários imutáveis com validação.
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

    private ProntuarioBuilder() {
    }

    public static ProntuarioBuilder builder() {
        return new ProntuarioBuilder();
    }

    public ProntuarioBuilder id(Integer id) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        return this;
    }

    public ProntuarioBuilder consulta(Consulta consulta) {
        this.consulta = Objects.requireNonNull(consulta, "Consulta não pode ser nula");
        return this;
    }

    public ProntuarioBuilder peso(double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero");
        }
        ProntuarioValidator.validarPeso(peso);
        this.peso = peso;
        return this;
    }

    public ProntuarioBuilder altura(double altura) {
        if (altura <= 0) {
            throw new IllegalArgumentException("Altura deve ser maior que zero");
        }
        ProntuarioValidator.validarAltura(altura);
        this.altura = altura;
        return this;
    }

    public ProntuarioBuilder sintomas(String sintomas) {
        this.sintomas = sintomas == null ? "" : sintomas;
        return this;
    }

    public ProntuarioBuilder observacao(String observacaoClinica) {
        this.observacaoClinica = observacaoClinica == null ? "" : observacaoClinica;
        return this;
    }

    public ProntuarioBuilder addPrescricao(Prescricao prescricao) {
        if (prescricao != null) {
            this.prescricoes.add(prescricao);
        }
        return this;
    }

    public ProntuarioBuilder addExame(Exame exame) {
        if (exame != null) {
            this.exames.add(exame);
        }
        return this;
    }

    public Prontuario build() {
        if (id == null) {
            throw new IllegalStateException("ID é obrigatório");
        }
        if (consulta == null) {
            throw new IllegalStateException("Consulta é obrigatória");
        }

        // Valida IMC como regra de negócio crítica
        ProntuarioValidator.calcularEValidarIMC(peso, altura);

        return new Prontuario(id, consulta, peso, altura, sintomas, observacaoClinica, prescricoes, exames);
    }
}
