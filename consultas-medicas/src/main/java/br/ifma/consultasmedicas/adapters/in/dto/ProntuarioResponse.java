package br.ifma.consultasmedicas.adapters.in.dto;

import br.ifma.consultasmedicas.core.domain.model.Prontuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO de resposta para Prontuário.
 * Mapeia a entidade de domínio para resposta HTTP/CLI.
 */
public class ProntuarioResponse {
    private final Integer id;
    private final Integer consultaId;
    private final String pacienteNome;
    private final String medicoNome;
    private final LocalDateTime dataConsulta;
    private final double peso;
    private final double altura;
    private final String sintomas;
    private final String observacaoClinica;
    private final List<PrescricaoItemResponse> prescricoes;
    private final List<ExameItemResponse> exames;

    public ProntuarioResponse(Integer id,
            Integer consultaId,
            String pacienteNome,
            String medicoNome,
            LocalDateTime dataConsulta,
            double peso,
            double altura,
            String sintomas,
            String observacaoClinica,
            List<PrescricaoItemResponse> prescricoes,
            List<ExameItemResponse> exames) {
        this.id = id;
        this.consultaId = consultaId;
        this.pacienteNome = pacienteNome;
        this.medicoNome = medicoNome;
        this.dataConsulta = dataConsulta;
        this.peso = peso;
        this.altura = altura;
        this.sintomas = sintomas;
        this.observacaoClinica = observacaoClinica;
        this.prescricoes = prescricoes;
        this.exames = exames;
    }

    public static ProntuarioResponse fromDomain(Prontuario prontuario) {
        return new ProntuarioResponse(
                prontuario.getId(),
                prontuario.getConsulta().getId(),
                prontuario.getConsulta().getPaciente().getNomeCrianca(),
                prontuario.getConsulta().getMedico().getNome(),
                prontuario.getConsulta().getDataHora(),
                prontuario.getPeso(),
                prontuario.getAltura(),
                prontuario.getSintomas(),
                prontuario.getObservacaoClinica(),
                prontuario.getPrescricoes().stream()
                        .map(PrescricaoItemResponse::fromDomain)
                        .collect(Collectors.toList()),
                prontuario.getExames().stream()
                        .map(ExameItemResponse::fromDomain)
                        .collect(Collectors.toList()));
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Integer getConsultaId() {
        return consultaId;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public String getMedicoNome() {
        return medicoNome;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public double getPeso() {
        return peso;
    }

    public double getAltura() {
        return altura;
    }

    public String getSintomas() {
        return sintomas;
    }

    public String getObservacaoClinica() {
        return observacaoClinica;
    }

    public List<PrescricaoItemResponse> getPrescricoes() {
        return prescricoes;
    }

    public List<ExameItemResponse> getExames() {
        return exames;
    }

    public static class PrescricaoItemResponse {
        private final Integer medicamentoId;
        private final String medicamentoNome;
        private final String dosagem;
        private final String administracao;
        private final String tempoUso;

        public PrescricaoItemResponse(Integer medicamentoId, String medicamentoNome, String dosagem,
                String administracao, String tempoUso) {
            this.medicamentoId = medicamentoId;
            this.medicamentoNome = medicamentoNome;
            this.dosagem = dosagem;
            this.administracao = administracao;
            this.tempoUso = tempoUso;
        }

        public static PrescricaoItemResponse fromDomain(
                br.ifma.consultasmedicas.core.domain.model.Prescricao prescricao) {
            return new PrescricaoItemResponse(
                    prescricao.getMedicamento().getId(),
                    prescricao.getMedicamento().getNome(),
                    prescricao.getDosagem(),
                    prescricao.getAdministracao(),
                    prescricao.getTempoUso());
        }

        public Integer getMedicamentoId() {
            return medicamentoId;
        }

        public String getMedicamentoNome() {
            return medicamentoNome;
        }

        public String getDosagem() {
            return dosagem;
        }

        public String getAdministracao() {
            return administracao;
        }

        public String getTempoUso() {
            return tempoUso;
        }
    }

    public static class ExameItemResponse {
        private final Integer exameId;
        private final String exameName;

        public ExameItemResponse(Integer exameId, String exameName) {
            this.exameId = exameId;
            this.exameName = exameName;
        }

        public static ExameItemResponse fromDomain(br.ifma.consultasmedicas.core.domain.model.Exame exame) {
            return new ExameItemResponse(exame.getId(), exame.getNome());
        }

        public Integer getExameId() {
            return exameId;
        }

        public String getExameName() {
            return exameName;
        }
    }
}
