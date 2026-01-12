package br.ifma.consultasmedicas.core.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Consulta {
    private final Integer id;
    private final Paciente paciente;
    private final Medico medico;
    private final LocalDateTime dataHora;
    private final boolean pacienteNovo;
    private ConsultaStatus status;

    public Consulta(Integer id, Paciente paciente, Medico medico, LocalDateTime dataHora, boolean pacienteNovo) {
        this.id = Objects.requireNonNull(id);
        this.paciente = Objects.requireNonNull(paciente);
        this.medico = Objects.requireNonNull(medico);
        this.dataHora = Objects.requireNonNull(dataHora);
        this.pacienteNovo = pacienteNovo;
        this.status = ConsultaStatus.AGENDADA;
    }

    public Integer getId() { return id; }
    public Paciente getPaciente() { return paciente; }
    public Medico getMedico() { return medico; }
    public LocalDateTime getDataHora() { return dataHora; }
    public boolean isPacienteNovo() { return pacienteNovo; }
    public ConsultaStatus getStatus() { return status; }

    public void marcarRealizada() {
        this.status = ConsultaStatus.REALIZADA;
    }

    public void cancelar() {
        this.status = ConsultaStatus.CANCELADA;
    }
}
