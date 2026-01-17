package br.ifma.consultasmedicas.core.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Entidade que representa uma Consulta agendada.
 * Invariante: Uma Consulta pode ter no máximo um Prontuário.
 * 
 * Responsabilidades:
 * - Manter dados da consulta agendada
 * - Gerenciar status (AGENDADA → REALIZADA ou CANCELADA)
 * - Referenciar o Prontuário gerado (quando realizada)
 */
public class Consulta {
    private final Integer id;
    private final Paciente paciente;
    private final Medico medico;
    private final LocalDateTime dataHora;
    private final boolean pacienteNovo;
    private ConsultaStatus status;
    private Integer prontuarioId; // ID do prontuário gerado (null se não realizada)

    public Consulta(Integer id, Paciente paciente, Medico medico, LocalDateTime dataHora, boolean pacienteNovo) {
        this.id = Objects.requireNonNull(id);
        this.paciente = Objects.requireNonNull(paciente);
        this.medico = Objects.requireNonNull(medico);
        this.dataHora = Objects.requireNonNull(dataHora);
        this.pacienteNovo = pacienteNovo;
        this.status = ConsultaStatus.AGENDADA;
        this.prontuarioId = null; // Inicialmente sem prontuário
    }

    public Integer getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public boolean isPacienteNovo() {
        return pacienteNovo;
    }

    public ConsultaStatus getStatus() {
        return status;
    }

    /**
     * Retorna o ID do prontuário gerado (se existir).
     */
    public Optional<Integer> getProntuarioId() {
        return Optional.ofNullable(prontuarioId);
    }

    /**
     * Marca a consulta como realizada e associa o prontuário gerado.
     * Invariante: Uma consulta só pode gerar um prontuário.
     * 
     * @param prontuarioId ID do prontuário gerado
     * @throws IllegalStateException se a consulta já foi realizada
     */
    public void marcarRealizada(Integer prontuarioId) {
        Objects.requireNonNull(prontuarioId, "ProntuarioId não pode ser nulo");

        if (this.status != ConsultaStatus.AGENDADA) {
            throw new IllegalStateException(
                    "Consulta com status " + status + " não pode ser marcada como realizada. " +
                            "Apenas consultas AGENDADAS podem ser realizadas.");
        }

        if (this.prontuarioId != null) {
            throw new IllegalStateException(
                    "Consulta já tem um prontuário associado (ID: " + this.prontuarioId + "). " +
                            "Não é permitido criar múltiplos prontuários para a mesma consulta.");
        }

        this.status = ConsultaStatus.REALIZADA;
        this.prontuarioId = prontuarioId;
    }

    /**
     * Marca a consulta como realizada (compatibilidade com código anterior).
     * Use preferencialmente marcarRealizada(prontuarioId).
     * 
     * @deprecated Use {@link #marcarRealizada(Integer)} em vez disso
     */
    @Deprecated
    public void marcarRealizada() {
        if (this.status != ConsultaStatus.AGENDADA) {
            throw new IllegalStateException("Consulta não está em estado AGENDADA");
        }
        this.status = ConsultaStatus.REALIZADA;
    }

    public void cancelar() {
        if (this.status == ConsultaStatus.REALIZADA) {
            throw new IllegalStateException(
                    "Consulta já realizada não pode ser cancelada. " +
                            "Consultas realizadas são permanentes.");
        }
        this.status = ConsultaStatus.CANCELADA;
    }
}
