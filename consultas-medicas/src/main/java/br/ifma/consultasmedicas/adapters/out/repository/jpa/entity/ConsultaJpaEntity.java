package br.ifma.consultasmedicas.adapters.out.repository.jpa.entity;

import jakarta.persistence.*;

/**
 * Entidade JPA para Consulta.
 * Separada da entidade de domínio para não poluir o core.
 */
@Entity
@Table(name = "consultas")
public class ConsultaJpaEntity {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "paciente_id", nullable = false)
    private Integer pacienteId;

    @Column(name = "medico_id", nullable = false)
    private Integer medicoId;

    @Column(name = "data_hora", nullable = false)
    private String dataHora; // ISO format

    @Column(name = "paciente_novo", nullable = false)
    private boolean pacienteNovo;

    @Column(name = "status", nullable = false)
    private String status; // AGENDADA, REALIZADA, CANCELADA

    public ConsultaJpaEntity() {
    }

    public ConsultaJpaEntity(Integer id, Integer pacienteId, Integer medicoId,
            String dataHora, boolean pacienteNovo, String status) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.dataHora = dataHora;
        this.pacienteNovo = pacienteNovo;
        this.status = status;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public boolean isPacienteNovo() {
        return pacienteNovo;
    }

    public void setPacienteNovo(boolean pacienteNovo) {
        this.pacienteNovo = pacienteNovo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
