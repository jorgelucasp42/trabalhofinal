package br.ifma.consultasmedicas.ports.in;

import java.util.List;

public class RegistrarProntuarioCommand {
    private final Integer consultaId;
    private final double peso;
    private final double altura;
    private final String sintomas;
    private final String observacaoClinica;
    private final List<PrescricaoItemCommand> prescricoes;
    private final List<Integer> examesIds;

    public RegistrarProntuarioCommand(Integer consultaId,
                                      double peso,
                                      double altura,
                                      String sintomas,
                                      String observacaoClinica,
                                      List<PrescricaoItemCommand> prescricoes,
                                      List<Integer> examesIds) {
        this.consultaId = consultaId;
        this.peso = peso;
        this.altura = altura;
        this.sintomas = sintomas;
        this.observacaoClinica = observacaoClinica;
        this.prescricoes = prescricoes;
        this.examesIds = examesIds;
    }

    public Integer getConsultaId() { return consultaId; }
    public double getPeso() { return peso; }
    public double getAltura() { return altura; }
    public String getSintomas() { return sintomas; }
    public String getObservacaoClinica() { return observacaoClinica; }
    public List<PrescricaoItemCommand> getPrescricoes() { return prescricoes; }
    public List<Integer> getExamesIds() { return examesIds; }

    public static class PrescricaoItemCommand {
        private final Integer medicamentoId;
        private final String dosagem;
        private final String administracao;
        private final String tempoUso;

        public PrescricaoItemCommand(Integer medicamentoId, String dosagem, String administracao, String tempoUso) {
            this.medicamentoId = medicamentoId;
            this.dosagem = dosagem;
            this.administracao = administracao;
            this.tempoUso = tempoUso;
        }

        public Integer getMedicamentoId() { return medicamentoId; }
        public String getDosagem() { return dosagem; }
        public String getAdministracao() { return administracao; }
        public String getTempoUso() { return tempoUso; }
    }
}
