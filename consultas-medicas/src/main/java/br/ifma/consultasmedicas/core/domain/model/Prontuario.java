package br.ifma.consultasmedicas.core.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Prontuario {
    private final Integer id;
    private final Consulta consulta;
    private final double peso;
    private final double altura;
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
        this.id = Objects.requireNonNull(id);
        this.consulta = Objects.requireNonNull(consulta);
        this.peso = peso;
        this.altura = altura;
        this.sintomas = Objects.requireNonNullElse(sintomas, "");
        this.observacaoClinica = Objects.requireNonNullElse(observacaoClinica, "");
        this.prescricoes = prescricoes == null ? new ArrayList<>() : new ArrayList<>(prescricoes);
        this.exames = exames == null ? new ArrayList<>() : new ArrayList<>(exames);
    }

    public Integer getId() { return id; }
    public Consulta getConsulta() { return consulta; }
    public double getPeso() { return peso; }
    public double getAltura() { return altura; }
    public String getSintomas() { return sintomas; }
    public String getObservacaoClinica() { return observacaoClinica; }
    public List<Prescricao> getPrescricoes() { return Collections.unmodifiableList(prescricoes); }
    public List<Exame> getExames() { return Collections.unmodifiableList(exames); }
}
