package br.ifma.consultasmedicas.core.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Agregado de Prontuário.
 * Encapsula uma consulta realizada com seus dados clínicos, prescrições e
 * exames.
 * 
 * Invariantes:
 * - Um prontuário pertence a exatamente uma consulta
 * - Peso está dentro de intervalo válido (0.5kg - 300kg)
 * - Altura está dentro de intervalo válido (0.3m - 2.5m)
 * - IMC calculado deve estar dentro de intervalo válido (5 - 100)
 */
public class Prontuario {
    private final Integer id;
    private final Consulta consulta;
    private final double peso;
    private final double altura;
    private final double imc;
    private final String sintomas;
    private final String observacaoClinica;
    private final List<Prescricao> prescricoes;
    private final List<Exame> exames;

    public Prontuario(Integer id,
            Consulta consulta,
            double peso,
            double altura,
            String sintomas,
            String observacaoClinica,
            List<Prescricao> prescricoes,
            List<Exame> exames) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.consulta = Objects.requireNonNull(consulta, "Consulta não pode ser nula");

        // Validações defensivas de peso e altura
        ProntuarioValidator.validarPeso(peso);
        this.peso = peso;

        ProntuarioValidator.validarAltura(altura);
        this.altura = altura;

        // Valida e calcula IMC
        this.imc = ProntuarioValidator.calcularEValidarIMC(peso, altura);

        this.sintomas = Objects.requireNonNullElse(sintomas, "");
        this.observacaoClinica = Objects.requireNonNullElse(observacaoClinica, "");
        this.prescricoes = prescricoes == null ? new ArrayList<>() : new ArrayList<>(prescricoes);
        this.exames = exames == null ? new ArrayList<>() : new ArrayList<>(exames);
    }

    public Integer getId() {
        return id;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public double getPeso() {
        return peso;
    }

    public double getAltura() {
        return altura;
    }

    public double getImc() {
        return imc;
    }

    public String getSintomas() {
        return sintomas;
    }

    public String getObservacaoClinica() {
        return observacaoClinica;
    }

    public List<Prescricao> getPrescricoes() {
        return Collections.unmodifiableList(prescricoes);
    }

    public List<Exame> getExames() {
        return Collections.unmodifiableList(exames);
    }

    /**
     * Classifica o IMC em categorias de risco.
     */
    public String classificarImc() {
        return ProntuarioValidator.classificarIMC(imc);
    }
}
