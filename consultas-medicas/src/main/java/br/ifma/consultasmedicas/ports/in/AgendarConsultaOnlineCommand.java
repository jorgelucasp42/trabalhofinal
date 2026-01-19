package br.ifma.consultasmedicas.ports.in;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Command para agendar consulta online.
 * 
 * Contém dados necessários para validação e criação da consulta.
 */
public class AgendarConsultaOnlineCommand {
    private final Integer pacienteId;
    private final Integer medicoId;
    private final LocalDateTime dataHora;
    private final String tipoVideoconferencia; // Zoom, Teams, Google Meet

    public AgendarConsultaOnlineCommand(Integer pacienteId, Integer medicoId,
            LocalDateTime dataHora, String tipoVideoconferencia) {
        this.pacienteId = Objects.requireNonNull(pacienteId, "ID do paciente não pode ser nulo");
        this.medicoId = Objects.requireNonNull(medicoId, "ID do médico não pode ser nulo");
        this.dataHora = Objects.requireNonNull(dataHora, "Data/hora não pode ser nula");
        this.tipoVideoconferencia = Objects.requireNonNull(tipoVideoconferencia,
                "Tipo de videoconferência não pode ser nulo");

        if (tipoVideoconferencia.isBlank()) {
            throw new IllegalArgumentException("Tipo de videoconferência não pode estar vazio");
        }
    }

    public Integer getPacienteId() {
        return pacienteId;
    }

    public Integer getMedicoId() {
        return medicoId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getTipoVideoconferencia() {
        return tipoVideoconferencia;
    }
}
