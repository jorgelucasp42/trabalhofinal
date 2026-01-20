package br.ifma.consultasmedicas.ports.in;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Command para agendar consulta presencial.
 * 
 * Contém dados necessários para validação e criação da consulta presencial.
 */
public class AgendarConsultaPresencialCommand {
    private final Integer pacienteId;
    private final Integer medicoId;
    private final LocalDateTime dataHora;
    private final boolean pacienteNovo;

    public AgendarConsultaPresencialCommand(Integer pacienteId, Integer medicoId,
            LocalDateTime dataHora, boolean pacienteNovo) {
        this.pacienteId = Objects.requireNonNull(pacienteId, "ID do paciente não pode ser nulo");
        this.medicoId = Objects.requireNonNull(medicoId, "ID do médico não pode ser nulo");
        this.dataHora = Objects.requireNonNull(dataHora, "Data/hora não pode ser nula");
        this.pacienteNovo = pacienteNovo;
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

    public boolean isPacienteNovo() {
        return pacienteNovo;
    }
}
