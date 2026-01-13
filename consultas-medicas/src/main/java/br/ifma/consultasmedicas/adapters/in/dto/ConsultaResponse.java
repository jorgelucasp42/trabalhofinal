package br.ifma.consultasmedicas.adapters.in.dto;

import br.ifma.consultasmedicas.core.domain.model.Consulta;

import java.time.LocalDateTime;

/**
 * DTO de resposta para listagem de consultas do dia.
 */
public class ConsultaResponse {
    private final Integer id;
    private final String pacienteNome;
    private final String medicoNome;
    private final LocalDateTime dataHora;
    private final boolean pacienteNovo;
    private final String status;

    public ConsultaResponse(Integer id, String pacienteNome, String medicoNome, LocalDateTime dataHora,
            boolean pacienteNovo, String status) {
        this.id = id;
        this.pacienteNome = pacienteNome;
        this.medicoNome = medicoNome;
        this.dataHora = dataHora;
        this.pacienteNovo = pacienteNovo;
        this.status = status;
    }

    public static ConsultaResponse fromDomain(Consulta consulta) {
        return new ConsultaResponse(
                consulta.getId(),
                consulta.getPaciente().getNomeCrianca(),
                consulta.getMedico().getNome(),
                consulta.getDataHora(),
                consulta.isPacienteNovo(),
                consulta.getStatus().toString());
    }

    public Integer getId() {
        return id;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public String getMedicoNome() {
        return medicoNome;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public boolean isPacienteNovo() {
        return pacienteNovo;
    }

    public String getStatus() {
        return status;
    }
}
