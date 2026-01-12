package br.ifma.consultasmedicas.core.domain.model;

import java.util.Objects;

public class Prescricao {
    private final Integer id;
    private final Medicamento medicamento;
    private final String dosagem;
    private final String administracao;
    private final String tempoUso;

    public Prescricao(Integer id, Medicamento medicamento, String dosagem, String administracao, String tempoUso) {
        this.id = Objects.requireNonNull(id);
        this.medicamento = Objects.requireNonNull(medicamento);
        this.dosagem = Objects.requireNonNull(dosagem);
        this.administracao = Objects.requireNonNull(administracao);
        this.tempoUso = Objects.requireNonNull(tempoUso);
    }

    public Integer getId() { return id; }
    public Medicamento getMedicamento() { return medicamento; }
    public String getDosagem() { return dosagem; }
    public String getAdministracao() { return administracao; }
    public String getTempoUso() { return tempoUso; }
}
