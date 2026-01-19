package br.ifma.consultasmedicas.adapters.in.dto;

import java.time.LocalDateTime;

/**
 * DTO para requisição de agendamento online.
 */
public class AgendarConsultaOnlineRequest {
    private Integer pacienteId;
    private Integer medicoId;
    private LocalDateTime dataHora;
    private String tipoVideoconferencia;

    public AgendarConsultaOnlineRequest() {
    }

    public AgendarConsultaOnlineRequest(Integer pacienteId, Integer medicoId,
            LocalDateTime dataHora, String tipoVideoconferencia) {
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.dataHora = dataHora;
        this.tipoVideoconferencia = tipoVideoconferencia;
    }

    public Integer getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Integer pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Integer getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Integer medicoId) {
        this.medicoId = medicoId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getTipoVideoconferencia() {
        return tipoVideoconferencia;
    }

    public void setTipoVideoconferencia(String tipoVideoconferencia) {
        this.tipoVideoconferencia = tipoVideoconferencia;
    }
}
